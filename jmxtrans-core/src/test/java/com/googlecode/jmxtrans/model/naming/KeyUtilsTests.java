/**
 * The MIT License
 * Copyright (c) 2010 JmxTrans team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jmxtrans.model.naming;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.googlecode.jmxtrans.model.naming.KeyUtils.getTypeNameValueMap;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Zack Radick Date: 1/20/12
 * @author Arthur Naseef Date: 02/23/2014
 */
public class KeyUtilsTests {

	@Test
	public void testGetTypeNameValueMap() {
		assertThat(getTypeNameValueMap(null)).isEmpty();
		assertThat(getTypeNameValueMap("")).isEmpty();
		assertThat(getTypeNameValueMap("x-key1-x")).isEqualTo(makeMap("x-key1-x", ""));
		assertThat(getTypeNameValueMap("x-key1-x,x-key2-x")).isEqualTo(makeMap("x-key1-x", "", "x-key2-x", ""));
		assertThat(getTypeNameValueMap("x-key1-x=x-value1-x")).isEqualTo(makeMap("x-key1-x", "x-value1-x"));

		assertThat(getTypeNameValueMap("x-key1-x=x-value1-x,y-key2-y=y-value2-y"))
				.isEqualTo(makeMap("x-key1-x", "x-value1-x", "y-key2-y", "y-value2-y"));
		assertThat(getTypeNameValueMap("x-key1-x=x-value1-x,y-key2-y=y-value2-y,z-key3-z=z-value3-z"))
				.isEqualTo(makeMap("x-key1-x", "x-value1-x", "y-key2-y", "y-value2-y", "z-key3-z", "z-value3-z"));
		assertThat(getTypeNameValueMap("x-key1-x=x-value1-x,y-key2-y,yy-key2.5-yy=a=1,z-key3-z=z-value3-z"))
				.isEqualTo(makeMap("x-key1-x", "x-value1-x", "y-key2-y", "", "yy-key2.5-yy", "a=1", "z-key3-z", "z-value3-z"));
	}

	@Test
	public void testGetConcatenatedTypeNameValues() {
		assertThat(KeyUtils.getConcatedTypeNameValues(null, "a=1")).isNull();
		assertThat(KeyUtils.getConcatedTypeNameValues(Collections.<String>emptyList(), "a=1")).isNull();
		assertThat(KeyUtils.getConcatedTypeNameValues(asList("x-key1-x"), "")).isEmpty();
		assertThat(KeyUtils.getConcatedTypeNameValues(asList("x-key1-x", "y-key2-y"), "")).isEmpty();
		assertThat(KeyUtils.getConcatedTypeNameValues(asList("x-key1-x"), "x-key1-x=x-value1-x")).isEqualTo("x-value1-x");
		assertThat(KeyUtils.getConcatedTypeNameValues(asList("x-key1-x"), "y-key2-y=y-value2-y")).isEmpty();
		assertThat(KeyUtils.getConcatedTypeNameValues(asList("x-key1-x", "y-key2-y"), "x-key1-x=x-value1-x,y-key2-y=y-value2-y"))
				.isEqualTo("x-value1-x_y-value2-y");
	}

	/** Convenience method for creating a Map for comparison. */
	private Map<String, String> makeMap(String... keysAndValues) {
		Map<String, String> result;
		int cur;

		result = newHashMap();

		cur = 0;
		while (cur < keysAndValues.length) {
			if (cur < keysAndValues.length - 1) {
				result.put(keysAndValues[cur], keysAndValues[cur + 1]);
				cur += 2;
			} else {
				result.put(keysAndValues[cur], "");
				cur++;
			}
		}

		return result;
	}
}
