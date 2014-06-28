package com.ldbc.driver.workloads.ldbc.snb.interactive;

import com.ldbc.driver.Operation;
import com.ldbc.driver.SerializingMarshallingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LdbcQuery10 extends Operation<List<LdbcQuery10Result>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final int DEFAULT_LIMIT = 20;
    private final long personId;
    private final String personUri;
    private final int month1;
    private final int month2;
    private final int limit;

    public LdbcQuery10(long personId, String personUri, int month1, int month2, int limit) {
        this.personId = personId;
        this.personUri = personUri;
        this.month1 = month1;
        this.month2 = month2;
        this.limit = limit;
    }

    public long personId() {
        return personId;
    }

    public String personUri() {
        return personUri;
    }

    public int month1() {
        return month1;
    }

    public int month2() {
        return month2;
    }

    public int limit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LdbcQuery10 that = (LdbcQuery10) o;

        if (limit != that.limit) return false;
        if (month1 != that.month1) return false;
        if (month2 != that.month2) return false;
        if (personId != that.personId) return false;
        if (personUri != null ? !personUri.equals(that.personUri) : that.personUri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (personId ^ (personId >>> 32));
        result = 31 * result + (personUri != null ? personUri.hashCode() : 0);
        result = 31 * result + month1;
        result = 31 * result + month2;
        result = 31 * result + limit;
        return result;
    }

    @Override
    public String toString() {
        return "LdbcQuery10{" +
                "personId=" + personId +
                ", personUri='" + personUri + '\'' +
                ", month1=" + month1 +
                ", month2=" + month2 +
                ", limit=" + limit +
                '}';
    }

    @Override
    public List<LdbcQuery10Result> marshalResult(String serializedResults) throws SerializingMarshallingException {
        List<List<Object>> resultsAsList;
        try {
            resultsAsList = objectMapper.readValue(serializedResults, new TypeReference<List<List<Object>>>() {
            });
        } catch (IOException e) {
            throw new SerializingMarshallingException(String.format("Error while parsing serialized results\n%s", serializedResults), e);
        }

        List<LdbcQuery10Result> results = new ArrayList<>();
        for (int i = 0; i < resultsAsList.size(); i++) {
            List<Object> resultAsList = resultsAsList.get(i);
            long personId = ((Number) resultAsList.get(0)).longValue();
            String personFirstName = (String) resultAsList.get(1);
            String personLastName = (String) resultAsList.get(2);
            double commonInterestScore = ((Number) resultAsList.get(3)).doubleValue();
            String personGender = (String) resultAsList.get(4);
            String personCityName = (String) resultAsList.get(5);

            results.add(new LdbcQuery10Result(
                    personId,
                    personFirstName,
                    personLastName,
                    commonInterestScore,
                    personGender,
                    personCityName
            ));
        }

        return results;
    }

    @Override
    public String serializeResult(Object resultsObject) throws SerializingMarshallingException {
        List<LdbcQuery10Result> results = (List<LdbcQuery10Result>) resultsObject;
        List<List<Object>> resultsFields = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            LdbcQuery10Result result = results.get(i);
            List<Object> resultFields = new ArrayList<>();
            resultFields.add(result.personId());
            resultFields.add(result.personFirstName());
            resultFields.add(result.personLastName());
            resultFields.add(result.commonInterestScore());
            resultFields.add(result.personGender());
            resultFields.add(result.personCityName());
            resultsFields.add(resultFields);
        }

        try {
            return objectMapper.writeValueAsString(resultsFields);
        } catch (IOException e) {
            throw new SerializingMarshallingException(String.format("Error while trying to serialize result\n%s", results.toString()), e);
        }
    }
}