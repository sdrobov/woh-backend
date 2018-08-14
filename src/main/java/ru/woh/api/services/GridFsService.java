package ru.woh.api.services;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class GridFsService {
    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    @Autowired
    public GridFsService(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
    }

    public String store(InputStream inputStream, String name, String mime, HashMap<String, String> meta) {
        BasicDBObject dbObject = new BasicDBObject();
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            dbObject.put(entry.getKey(), entry.getValue());
        }

        return this.gridFsTemplate.store(inputStream, name, mime, dbObject).toHexString();
    }

    public GridFSFile findById(String id) {
        return this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }

    public GridFSFindIterable findByKeyValue(String key, String value) {
        return this.gridFsTemplate.find(new Query(Criteria.where(String.format("metadata.%s", key)).is(value)));
    }

    public byte[] getFile(GridFSFile gridFSFile) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.gridFSBucket.downloadToStream(gridFSFile.getId(), os);

        return os.toByteArray();
    }

    public void delete(GridFSFile gridFSFile) {
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(gridFSFile.getId())));
    }
}
