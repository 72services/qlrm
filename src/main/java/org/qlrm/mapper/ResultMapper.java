/*
 * Copyright 2013 s.martinelli.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qlrm.mapper;

import java.lang.reflect.Constructor;

/**
 *
 * @author s.martinelli
 */
public abstract class ResultMapper {

    @SuppressWarnings(value = "unchecked")
    protected <T> T createInstance(Constructor<?> ctor, Object[] args) {
        try {
            return (T) ctor.newInstance(args);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("no constructor taking:\n");
            for (Object object : args) {
                sb.append("\t").append(object.getClass().getName()).append("\n");
            }
            throw new RuntimeException(sb.toString(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
