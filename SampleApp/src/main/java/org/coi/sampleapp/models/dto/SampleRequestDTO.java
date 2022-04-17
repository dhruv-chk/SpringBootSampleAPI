package org.coi.sampleapp.models.dto;

import com.google.gson.JsonArray;

public class SampleRequestDTO {
    public long table_id;
    public String startTime;
    public String endTime;
    public JsonArray values;
}
