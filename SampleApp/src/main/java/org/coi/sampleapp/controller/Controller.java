package org.coi.sampleapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.coi.sampleapp.config.DBConfig;
import org.coi.sampleapp.models.dto.SampleResponseDTO;
import org.coi.sampleapp.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;

@RestController
public class Controller {
    DBConfig dbConfig = new DBConfig();
    final static Logger logger = LogManager.getLogger(Controller.class);

    @CrossOrigin
    @GetMapping
    public ResponseEntity<SampleResponseDTO> getRequest(@RequestHeader String tenant_id){
        try{
            logger.info("Sample API API Invoked.");
            String env = System.getenv("ENV_TYPE");
            String persistence_unit = tenant_id + "_" + env;
            EntityManager em = dbConfig.getEntity(persistence_unit);
            Service service = new Service(em);

            SampleResponseDTO sampleResponseDTO = service.getSummary(tenant_id);
            if(sampleResponseDTO == null)
                return new ResponseEntity<>((SampleResponseDTO) null,
                        HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(sampleResponseDTO, HttpStatus.OK);

        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>((SampleResponseDTO) null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("set_data")
    public ResponseEntity setData(@RequestHeader String tenant_id){
        try{
            String env = System.getenv("ENV_TYPE");
            String persistence_unit = tenant_id + "_" + env;
            EntityManager em = dbConfig.getEntity(persistence_unit);
            Service service = new Service(em);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>((SampleResponseDTO) null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
