package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.component.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

import java.util.List;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {
    
	private final RestTemplate restTemplate;

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        HttpEntity<DataEnvelope> request = new HttpEntity<DataEnvelope>(dataEnvelope);
    	Boolean isSuccessfullyPushed = restTemplate.postForObject(URI_PUSHDATA, request,Boolean.class);
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        return Arrays.asList(restTemplate.getForObject(URI_GETDATA.expand(blockType),DataEnvelope[].class));
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        return true;
    }


}
