package org.apache.samza.system.cosmosdb;

import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemProducer;

public class CosmosDBSystemProducer implements SystemProducer {
  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @Override
  public void register(String source) {
  }

  @Override
  public void send(String source, OutgoingMessageEnvelope envelope) {
  }

  @Override
  public void flush(String source) {
  }
}
