/*
 * Copyright 2016 CREATE-NET
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
package org.createnet.raptor.models.data;

import java.util.Date;
import org.createnet.raptor.models.objects.Channel;
import org.createnet.raptor.models.objects.RaptorComponent;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public interface IRecord<K> {
    
    public Date getTimestamp();
    public void setTimestamp(Date date);
    
    public String getType();
    public Class<K> getClassType();
    public String getName();
    
    public K getValue();
    public K parseValue(Object raw) throws RaptorComponent.ParserException;
    
    public void setValue(Object value) throws RaptorComponent.ParserException;

    public Channel getChannel();
    public void setChannel(Channel channel);

    public Long getTimestampTime();
    
    public RecordSet getRecordSet();
    public void setRecordSet(RecordSet recordset);
}
