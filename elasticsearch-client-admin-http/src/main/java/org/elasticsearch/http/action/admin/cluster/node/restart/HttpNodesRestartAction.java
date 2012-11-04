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

package org.elasticsearch.http.action.admin.cluster.node.restart;

import java.io.IOException;
import java.util.Map;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartRequest;
import org.elasticsearch.action.admin.cluster.node.restart.NodesRestartResponse;
import org.elasticsearch.action.support.HttpAction;
import org.elasticsearch.action.support.HttpRequest;
import org.elasticsearch.action.support.HttpResponse;
import org.elasticsearch.client.http.HttpClusterAdminClient;
import org.elasticsearch.common.xcontent.XContentHelper;


public class HttpNodesRestartAction extends HttpAction<HttpClusterAdminClient, NodesRestartRequest, NodesRestartResponse>{

    public static final String NAME = "cluster_nodes_restart";
    
    private static final String METHOD = "POST";
    
    private static final String ENDPOINT = "/_cluster/nodes/_restart";
    
    @Override
    protected void doExecute(HttpClusterAdminClient client, NodesRestartRequest request, ActionListener<NodesRestartResponse> listener) {
        HttpRequest httpRequest = new HttpRequest(client.settings(), METHOD, ENDPOINT)
                .param("nodeId", request.nodesIds())
                .param("delay", request.delay())
                .param("timeout", request.timeout())
                ;
        submit(client, httpRequest, listener);        
    }

    @Override
    protected NodesRestartResponse toResponse(HttpResponse response) throws IOException {
        Map<String, Object> map = XContentHelper.convertToMap(response.getBody(), false).v2();
        logger.info("response = {}", map);
        return null;
    }
    
}
