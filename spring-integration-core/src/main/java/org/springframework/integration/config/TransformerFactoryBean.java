/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.integration.config;

import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.transformer.ExpressionEvaluatingTransformer;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.integration.transformer.MethodInvokingTransformer;
import org.springframework.integration.transformer.Transformer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Factory bean for creating a Message Transformer.
 * 
 * @author Mark Fisher
 */
public class TransformerFactoryBean extends AbstractMessageHandlerFactoryBean {

	private volatile Long sendTimeout;

	public void setSendTimeout(Long sendTimeout) {
		this.sendTimeout = sendTimeout;
	}

	@Override
	MessageHandler createMethodInvokingHandler(Object targetObject, String targetMethodName) {
		Assert.notNull(targetObject, "targetObject must not be null");
		Transformer transformer = null;
		if (targetObject instanceof Transformer) {
			transformer = (Transformer) targetObject;
		}
		else if (StringUtils.hasText(targetMethodName)) {
			transformer = new MethodInvokingTransformer(targetObject, targetMethodName);
		}
		else {
			transformer = new MethodInvokingTransformer(targetObject);
		}
		return this.createHandler(transformer);
	}

	@Override
	MessageHandler createExpressionEvaluatingHandler(String expression) {
		Transformer transformer = new ExpressionEvaluatingTransformer(expression);
		return this.createHandler(transformer);
	}

	private MessageTransformingHandler createHandler(Transformer transformer) {
		MessageTransformingHandler handler = new MessageTransformingHandler(transformer);
		if (this.sendTimeout != null) {
			handler.setSendTimeout(this.sendTimeout.longValue());
		}
		return handler;
	}

}
