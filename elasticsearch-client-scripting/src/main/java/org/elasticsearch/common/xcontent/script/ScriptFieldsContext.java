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

package org.elasticsearch.common.xcontent.script;

import com.google.common.collect.Lists;
import org.elasticsearch.script.ContentScript;

import java.util.List;

/**
 *
 */
public class ScriptFieldsContext {

    public static class ScriptField {
        private final String name;
        private final ContentScript script;
        private final boolean ignoreException;

        public ScriptField(String name, ContentScript script, boolean ignoreException) {
            this.name = name;
            this.script = script;
            this.ignoreException = ignoreException;
        }

        public String name() {
            return name;
        }

        public ContentScript script() {
            return this.script;
        }

        public boolean ignoreException() {
            return ignoreException;
        }
    }

    private List<ScriptField> fields = Lists.newArrayList();

    public ScriptFieldsContext() {
    }

    public void add(ScriptField field) {
        this.fields.add(field);
    }

    public List<ScriptField> fields() {
        return this.fields;
    }
}
