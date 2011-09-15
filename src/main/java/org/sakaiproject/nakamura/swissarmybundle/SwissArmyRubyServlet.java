/*
 * Licensed to the Sakai Foundation (SF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.sakaiproject.nakamura.swissarmybundle;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.jcr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.servlets.*;
import org.apache.sling.api.*;
import javax.servlet.*;

import org.sakaiproject.nakamura.api.lite.Repository;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;


@SlingServlet(methods = "GET", paths = "/system/swissarmy/ruby", generateComponent=true)
public class SwissArmyRubyServlet extends SlingSafeMethodsServlet
{
    @Reference
    SwissArmyService sas;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
        throws ServletException, IOException
    {
        sas.launchRuby (request, response, new HashMap(), this);
    }
}
