package com.rdpk.mars.console;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.rdpk.mars.console.CommandType.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProtocolCommandParserTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void creating_a_plateau() {
    ProtocolCommandParser parser = new ProtocolCommandParser("5 5");
    parser.parse();
    assertThat(CREATE).isEqualTo(parser.type);
    assertThat(new Tuple<>(5, 5)).isEqualTo(parser.create());
  }

  @Test
  void activate_rover() {
    ProtocolCommandParser parser = new ProtocolCommandParser("1 2 N");
    parser.parse();
    assertThat(ACTIVATE).isEqualTo(parser.type);
    assertThat(new ActivateRoover(1, 2, 'N').toString()).isEqualTo(parser.activate().toString());
  }

  @Test
  void move_rover() {
    ProtocolCommandParser parser = new ProtocolCommandParser("LMLMLMLMM");
    parser.parse();
    assertThat(MOVE).isEqualTo(parser.type);
  }
}