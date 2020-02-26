package ru.woh.api.services;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class GridFsService {
    private final GridFsOperations gridFsOperations;
    private final Logger logger = LoggerFactory.getLogger(GridFsService.class);

    @Autowired
    public GridFsService(GridFsOperations gridFsOperations) {
        this.gridFsOperations = gridFsOperations;
    }

    public String store(InputStream inputStream, String name, String mime, HashMap<String, String> meta) {
        BasicDBObject dbObject = new BasicDBObject();
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            dbObject.put(entry.getKey(), entry.getValue());
        }

        return this.gridFsOperations.store(inputStream, name, mime, dbObject).toHexString();
    }

    public GridFSFile findById(String id) {
        return this.gridFsOperations.findOne(new Query(Criteria.where("_id").is(id)));
    }

    public GridFSFindIterable findByKeyValue(String key, String value) {
        return this.gridFsOperations.find(new Query(Criteria.where(String.format("metadata.%s", key)).is(value)));
    }

    public byte[] getFile(GridFSFile gridFSFile) {
        byte[] data;
        var file = this.gridFsOperations.getResource(gridFSFile);

        try (var is = file.getInputStream()) {
            data = StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            this.logger.error("Cant read file" + gridFSFile.getFilename(), e);

            return null;
        }

        return data;
    }

    public void delete(GridFSFile gridFSFile) {
        this.gridFsOperations.delete(new Query(Criteria.where("_id").is(gridFSFile.getId())));
    }
}
