package com.db.dataplatform.techtest.server.component.impl;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.List;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) {
        
        //Calculate and compare checksum
    	byte[] data = envelope.getDataBody().getDataBody().getBytes();
    	try {
        	byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        	String calculatedChecksum = new BigInteger(1, hash).toString(16);
        	if(!calculatedChecksum.equals(envelope.getDataHeader().getChecksum())){
        	    log.error("Invalid checksum for the data provided. Data not persisted.");
        		return false;
        	}
    	} catch(NoSuchAlgorithmException nsaExecption) {
    	    log.error("NoSuchAlgorithmException raised. Data not persisted.");
    		return false;
    	}
        // Save to persistence.
        persist(envelope);

        log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
        return true;
    }
    
    public List<DataEnvelope> getDataEnvelopesByBlocktype(BlockTypeEnum blocktype){
        return dataBodyServiceImpl.getDataByBlockType(blocktype)
                .stream()
                .map(x -> modelMapper.map(x, DataEnvelope.class))
                .collect(Collectors.toList());
    }

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

}
