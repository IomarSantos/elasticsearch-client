/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.deletebyquery;

import com.google.common.base.Charsets;
import org.elasticsearch.ElasticSearchGenerationException;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.replication.IndicesReplicationOperationRequest;
import org.elasticsearch.client.SearchRequests;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Required;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.elasticsearch.action.ValidateActions.addValidationError;

/**
 * A request to delete all documents that matching a specific query. Best created with
 * {@link org.elasticsearch.client.Requests#deleteByQueryRequest(String...)}.
 * <p/>
 * <p>The request requires the query source to be set either using {@link #query(org.elasticsearch.index.query.QueryBuilder)},
 * or {@link #query(byte[])}.
 *
 * @see DeleteByQueryResponse
 * @see org.elasticsearch.client.Requests#deleteByQueryRequest(String...)
 * @see org.elasticsearch.client.Client#deleteByQuery(DeleteByQueryRequest)
 */
public class DeleteByQueryRequest extends IndicesReplicationOperationRequest<DeleteByQueryRequest> {

    private static final XContentType contentType = SearchRequests.CONTENT_TYPE;

    private BytesReference querySource;
    private boolean querySourceUnsafe;

    private String[] types = Strings.EMPTY_ARRAY;
    @Nullable
    private String routing;

    /**
     * Constructs a new delete by query request to run against the provided indices. No indices means
     * it will run against all indices.
     */
    public DeleteByQueryRequest(String... indices) {
        this.indices = indices;
    }

    public DeleteByQueryRequest() {
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = super.validate();
        if (querySource == null) {
            validationException = addValidationError("query is missing", validationException);
        }
        return validationException;
    }

    /**
     * The query source to execute.
     */
    BytesReference querySource() {
        if (querySourceUnsafe) {
            querySource = querySource.copyBytesArray();
        }
        return querySource;
    }

    /**
     * The query source to execute.
     *
     * @see org.elasticsearch.index.query.QueryBuilders
     */
    @Required
    public DeleteByQueryRequest query(QueryBuilder queryBuilder) {
        this.querySource = queryBuilder.buildAsBytes();
        this.querySourceUnsafe = false;
        return this;
    }

    /**
     * The query source to execute. It is preferable to use either {@link #query(byte[])}
     * or {@link #query(org.elasticsearch.index.query.QueryBuilder)}.
     */
    @Required
    public DeleteByQueryRequest query(String querySource) {
        this.querySource = new BytesArray(querySource.getBytes(Charsets.UTF_8));
        this.querySourceUnsafe = false;
        return this;
    }

    /**
     * The query source to execute in the form of a map.
     */
    @Required
    public DeleteByQueryRequest query(Map querySource) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.map(querySource);
            return query(builder);
        } catch (IOException e) {
            throw new ElasticSearchGenerationException("Failed to generate [" + querySource + "]", e);
        }
    }

    @Required
    public DeleteByQueryRequest query(XContentBuilder builder) {
        this.querySource = builder.bytes();
        this.querySourceUnsafe = false;
        return this;
    }

    /**
     * The query source to execute.
     */
    @Required
    public DeleteByQueryRequest query(byte[] querySource) {
        return query(querySource, 0, querySource.length, false);
    }

    /**
     * The query source to execute.
     */
    @Required
    public DeleteByQueryRequest query(byte[] querySource, int offset, int length, boolean unsafe) {
        this.querySource = new BytesArray(querySource, offset, length);
        this.querySourceUnsafe = unsafe;
        return this;
    }

    public DeleteByQueryRequest query(BytesReference source, boolean unsafe) {
        this.querySource = source;
        this.querySourceUnsafe = unsafe;
        return this;
    }

    /**
     * The types of documents the query will run against. Defaults to all types.
     */
    String[] types() {
        return this.types;
    }

    /**
     * A comma separated list of routing values to control the shards the search will be executed on.
     */
    public String routing() {
        return this.routing;
    }

    /**
     * A comma separated list of routing values to control the shards the search will be executed on.
     */
    public DeleteByQueryRequest routing(String routing) {
        this.routing = routing;
        return this;
    }

    /**
     * The routing values to control the shards that the search will be executed on.
     */
    public DeleteByQueryRequest routing(String... routings) {
        this.routing = Strings.arrayToCommaDelimitedString(routings);
        return this;
    }

    /**
     * The types of documents the query will run against. Defaults to all types.
     */
    public DeleteByQueryRequest types(String... types) {
        this.types = types;
        return this;
    }

    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        querySourceUnsafe = false;
        querySource = in.readBytesReference();
        routing = in.readOptionalString();
        types = in.readStringArray();
    }

    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeBytesReference(querySource);
        out.writeOptionalString(routing);
        out.writeStringArray(types);
    }

    @Override
    public String toString() {
        String sSource = "_na_";
        try {
            sSource = XContentHelper.convertToJson(querySource, false);
        } catch (Exception e) {
            // ignore
        }
        return "[" + Arrays.toString(indices) + "][" + Arrays.toString(types) + "], querySource[" + sSource + "]";
    }
}