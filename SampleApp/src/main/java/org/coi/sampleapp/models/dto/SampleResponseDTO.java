package org.coi.sampleapp.models.dto;

public class SampleResponseDTO {
    public Long table_id;
    public Long user_id;

    public SampleResponseDTO() {
    }

    public SampleResponseDTO(Long table_id, Long user_id) {
        this.table_id = table_id;
        this.user_id = user_id;
    }
}
