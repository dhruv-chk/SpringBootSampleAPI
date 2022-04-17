package org.coi.sampleapp.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import java.util.List;

public class AthenaService {

    private final String envType;
    private final String dbname;
    private final String bucket;

    public AthenaService() {
        this.envType = System.getenv("ENV_TYPE");
        this.dbname = "sample_db";
        this.bucket = "s3://sample_s3_bucket";
    }

    public double queryAthena(String query) throws InterruptedException {
        AthenaClient athenaClient = AthenaClient.builder()
                .region(Region.US_EAST_1)
                .build();

        String queryExecutionId = submitAthenaQuery(athenaClient, query);
        waitForQueryToComplete(athenaClient, queryExecutionId);
        double result = processResultRows(athenaClient, queryExecutionId);
        athenaClient.close();

        return result;
    }
    // Submits a sample query to Amazon Athena and returns the execution ID of the query
    public String submitAthenaQuery(AthenaClient athenaClient, String query) {

        try {

            // The QueryExecutionContext allows us to set the database
            QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder()
                    .database(dbname).build();

            // The result configuration specifies where the results of the query should go
            ResultConfiguration resultConfiguration = ResultConfiguration.builder()
                    .outputLocation(bucket)
                    .build();

            //"SELECT SUM(emission_generated) FROM \"coi_dev_carbon_emission\".\"coi_carbon_emission_dev\" where meter='313' and year='2021'"
            StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
                    .queryString(query)
                    .queryExecutionContext(queryExecutionContext)
                    .   resultConfiguration(resultConfiguration)
                    .build();

            StartQueryExecutionResponse startQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest);
            return startQueryExecutionResponse.queryExecutionId();

        } catch (AthenaException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }

    // Wait for an Amazon Athena query to complete, fail or to be cancelled
    public void waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId) throws InterruptedException {
        GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryExecutionResponse getQueryExecutionResponse;
        boolean isQueryStillRunning = true;
        while (isQueryStillRunning) {
            getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
            String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();
            if (queryState.equals(QueryExecutionState.FAILED.toString())) {
                throw new RuntimeException("The Amazon Athena query failed to run with error message: " + getQueryExecutionResponse
                        .queryExecution().status().stateChangeReason());
            } else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
                throw new RuntimeException("The Amazon Athena query was cancelled.");
            } else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
                isQueryStillRunning = false;
            } else {
                // Sleep an amount of time before retrying again
                Thread.sleep(1000);
            }
            System.out.println("The current status is: " + queryState);
        }
    }

    // This code retrieves the results of a query
    public double processResultRows(AthenaClient athenaClient, String queryExecutionId) {

        try {

            // Max Results can be set but if its not set,
            // it will choose the maximum page size
            GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                    .queryExecutionId(queryExecutionId)
                    .build();

            GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

            double emission = 0.d;
            for (GetQueryResultsResponse result : getQueryResultsResults) {
                List<ColumnInfo> columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
                List<Row> results = result.resultSet().rows();
                emission = processRow(results, columnInfoList);
            }
            return emission;
        } catch (AthenaException e) {
            e.printStackTrace();
            return 0.d;
        }
    }

    private double processRow(List<Row> row, List<ColumnInfo> columnInfoList) {
        double result = 0.d;
        try{
            List<Datum> allData = row.get(1).data();
            for (Datum data : allData) {
                try{
                    if(data != null) {
                        result = Double.parseDouble(data.varCharValue());
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
