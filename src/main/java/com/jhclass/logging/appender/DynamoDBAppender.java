package com.jhclass.logging.appender;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="DynamoDBAppender", category="Core", elementType="appender", printObject=true)
public final class DynamoDBAppender extends AbstractAppender {
  static Logger logger = Logger.getLogger(DynamoDBAppender.class.getName());

  public DynamoDBAppender(String name, Filter filter,
      Layout<? extends Serializable> layout,
      boolean ignoreExceptions, Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);
  }


  @Override
  public void append(LogEvent logEvent) {
    final byte[] bytes = getLayout().toByteArray(logEvent);
    try {
      String logJson = new String(bytes,"UTF-8");
      logger.log(Level.INFO,"logjson:"+logJson);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @PluginFactory
  public static DynamoDBAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginElement("Layout") Layout<? extends Serializable> layout,
      @PluginElement("Filter") final Filter filter,
      @PluginAttribute("target") String otherAttribute,
      @PluginElement("Properties") Property[] properties) {
    if (name == null) {
      logger.log(Level.WARNING,"No name provided for DynamoDBAppender");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new DynamoDBAppender(name, filter, layout, true,properties);
  }


}
