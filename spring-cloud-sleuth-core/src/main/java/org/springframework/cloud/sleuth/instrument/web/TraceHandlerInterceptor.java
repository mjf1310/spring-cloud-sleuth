/*
 * Copyright 2013-2015 the original author or authors.
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

package org.springframework.cloud.sleuth.instrument.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Spencer Gibb
 */
public class TraceHandlerInterceptor implements HandlerInterceptor {

	private static final String ATTR_NAME = "__CURRENT_TRACE_HANDLER_TRACE_SCOPE_ATTR___";

	private final TraceManager traceManager;

	public TraceHandlerInterceptor(TraceManager traceManager) {
		this.traceManager = traceManager;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		// TODO: get trace data from request?
		// TODO: what is the description?
		Trace trace = this.traceManager.startSpan("traceHandlerInterceptor");
		request.setAttribute(ATTR_NAME, trace);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		Trace trace = Trace.class.cast(request.getAttribute(ATTR_NAME));
		this.traceManager.close(trace);
	}
}
