/*
* Copyright 2007 the original author or authors.
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
package injecto.property;

/**
 * 
 * @author Luke Daley <ld@ldaley.com>
 */
class InjectoPropertyStorage 
{
	/**
	 * The properties
	 */
	static private Map properties = Collections.synchronizedMap(new WeakHashMap())
	
	static private Map defaults = [:]
	
	/**
	 * Allows use of the [] operator on the class.
	 * 
	 * @return Will always be a map.
	 */
	static Object getAt(owner)
	{
		if (properties[owner] == null)
		{
			properties[owner] = [:]
			if ((owner instanceof Class) == false)
			{
				List defaults = defaults[owner.class]
				def instanceMap = [:]
				defaults.each { aDefault ->
					if (instanceMap.containsKey(aDefault.injecto) == false) instanceMap[aDefault.injecto] = aDefault.injecto.newInstance()
					properties[owner][aDefault.propertyName] = instanceMap[aDefault.injecto]."${aDefault.fieldName}"
				}
			}
		}
 		return properties[owner]
	}
	
	static addDefaultFor(Class injectee, Class injecto, String propertyName, String fieldName)
	{
		if (defaults.containsKey(injectee) == false) defaults[injectee] = []
		defaults[injectee] << [injecto: injecto, propertyName: propertyName, fieldName: fieldName]
	}
}