package org.coi.sampleapp.services;

import org.coi.sampleapp.models.dao.*;
import org.coi.sampleapp.models.dto.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Service {

    EntityManager em;
    TableDAO tableDAO;

    public Service(EntityManager em) {
        this.em = em;
        this.tableDAO = new TableDAO(em);
    }

    public SampleResponseDTO getSummary(String tenant) throws IOException, InterruptedException {
        try{
            //ToDo
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return new SampleResponseDTO(1L,1L);
    }
    Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }
}

