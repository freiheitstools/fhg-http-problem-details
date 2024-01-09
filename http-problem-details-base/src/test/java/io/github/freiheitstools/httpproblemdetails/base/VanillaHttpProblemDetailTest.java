package io.github.freiheitstools.httpproblemdetails.base;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class VanillaHttpProblemDetailTest {

  VanillaHttpProblemDetail problemDetails;

  @BeforeEach
  void setUp() {
    problemDetails = new VanillaHttpProblemDetail();
  }

  @Test
  void settingStatusWithNullParameterCausesNullPointerException() {
    Assertions.assertThatThrownBy(() -> problemDetails.setStatus(null)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void setStatusWorks() {
    problemDetails.setStatus(999);

    assertThat(problemDetails.getStatus()).isPresent().get(as(INTEGER)).isEqualTo(999);
  }

  @Test
  void settingDetailWithNullParameterCausesNullPointerException() {
    Assertions.assertThatThrownBy(() -> problemDetails.setDetail(null)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void setDetailIsImmutable() {
    StringBuilder detailBuilder = new StringBuilder("An explanation");

    problemDetails.setDetail(detailBuilder);

    detailBuilder.append("X");

    assertThat(problemDetails.getDetail()).isPresent().get(as(STRING)).isEqualTo("An explanation");
    // Title can't be manipulated indirectly afterward
    assertThat(detailBuilder).hasToString("An explanationX");
  }

  @Test
  void settingTitleWithNullParameterCausesNullPointerException() {
    Assertions.assertThatThrownBy(() -> problemDetails.setTitle(null)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void setTitleIsImmutable() {
    StringBuilder titleBuilder = new StringBuilder("A title");

    problemDetails.setTitle(titleBuilder);

    titleBuilder.append("X");

    assertThat(problemDetails.getTitle()).isPresent().get(as(STRING)).isEqualTo("A title");
    // Title can't be manipulated indirectly afterward
    assertThat(titleBuilder).hasToString("A titleX");
  }

  @Test
  void toStringWorksIfStatusAndTitleAreGiven() {
    problemDetails.setTitle("ok");
    problemDetails.setStatus(200);

    assertThat(problemDetails).hasToString("200 - ok");
  }

  @Test
  void toStringWorksIfStatusAndNoTitleIsGiven() {
    problemDetails.setStatus(200);

    assertThat(problemDetails).hasToString("200");
  }

  @Test
  void toStringWorksIfNoStatusAndButTitleIsGiven() {
    problemDetails.setTitle("ok");

    assertThat(problemDetails).hasToString("ok");
  }

  @Test
  void toStringWorksIfNoStatusAndNoTitleIsGiven() {
    assertThat(problemDetails).hasToString("n/a");
  }

  @Test
  void setInstanceWithStringParameterWithNullCausesNullPointerException() {
    assertThatThrownBy(() -> problemDetails.setInstance((String)null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void setInstanceWithURIParameterWithNullCausesNullPointerException() {
    assertThatThrownBy(() -> problemDetails.setInstance((URI)null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void setInstanceWorksForStringParameter() {
    URI uri = URI.create("error:aboutAll");

    problemDetails.setInstance(uri.toString());

    assertThat(problemDetails.getInstance()).get(as(URI_TYPE)).isEqualTo(uri);
  }

  @Test
  void setInstanceWorksForURIParameter() {
    URI uri = URI.create("error:aboutAll");

    problemDetails.setInstance(uri);

    assertThat(problemDetails.getInstance()).get(as(URI_TYPE)).isEqualTo(uri);
  }

  @Test
  void setInstanceWithStringParameterWithInvalidURICausesInvalidArgumentException() {
    assertThatThrownBy(() -> problemDetails.setInstance(" about:error")).isInstanceOf(IllegalArgumentException.class);
  }

  void setTypeWithStringParameterWithInvalidURICausesInvalidArgumentException() {
    assertThatThrownBy(() -> problemDetails.setType(" about:error")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void setTypeWithStringParameterWithNullCausesNullPointerException() {
    assertThatThrownBy(() -> problemDetails.setType((String)null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void setTypeWithURIParameterWithNullCausesNullPointerException() {
    assertThatThrownBy(() -> problemDetails.setType((URI)null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void setTypeWorksForStringParameter() {
    URI uri = URI.create("error:aboutAll");

    problemDetails.setType(uri.toString());

    assertThat(problemDetails.getType()).get(as(URI_TYPE)).isEqualTo(uri);
  }

  @Test
  void setTypeWorksForURIParameter() {
    URI uri = URI.create("error:aboutAll");

    problemDetails.setType(uri);

    assertThat(problemDetails.getType()).get(as(URI_TYPE)).isEqualTo(uri);
  }

  @ArgumentsSource(ArgumentSourceForValidValues.class)
  @ParameterizedTest
  void equalsWorksForEqualValues(String instance, String type, Integer status, String title, String detail) {
    List<Consumer<VanillaHttpProblemDetail>> setter = List.of((VanillaHttpProblemDetail details) -> details.setInstance(instance),
                                                               (VanillaHttpProblemDetail details) -> details.setType(type),
                                                               (VanillaHttpProblemDetail details) -> details.setStatus(status),
                                                               (VanillaHttpProblemDetail details) -> details.setTitle(title),
                                                               (VanillaHttpProblemDetail details) -> details.setDetail(detail));

    VanillaHttpProblemDetail one = new VanillaHttpProblemDetail();
    VanillaHttpProblemDetail other = new VanillaHttpProblemDetail();

    setter.forEach(consumer -> consumer.accept(one));
    setter.forEach(consumer -> consumer.accept(other));

    assertThat(one).isEqualTo(other).hasSameHashCodeAs(other);
  }

  @ArgumentsSource(ArgumentSourceForValidValues.class)
  @ParameterizedTest
  void equalsWorksForNonEqualValues(String instance, String type, Integer status, String title, String detail) {
    List<Consumer<VanillaHttpProblemDetail>> setter = List.of((VanillaHttpProblemDetail details) -> details.setInstance(instance),
                                                               (VanillaHttpProblemDetail details) -> details.setType(type),
                                                               (VanillaHttpProblemDetail details) -> details.setStatus(status),
                                                               (VanillaHttpProblemDetail details) -> details.setTitle(title),
                                                               (VanillaHttpProblemDetail details) -> details.setDetail(detail));


    List<Consumer<VanillaHttpProblemDetail>> otherValueSetter =
      List.of((VanillaHttpProblemDetail details) -> details.setInstance("other:other"),
              (VanillaHttpProblemDetail details) -> details.setType("other:other"),
              (VanillaHttpProblemDetail details) -> details.setStatus(500),
              (VanillaHttpProblemDetail details) -> details.setTitle("TITLE"),
              (VanillaHttpProblemDetail details) -> details.setDetail("DETAILS"));

    int currentRound = 0;

    for (int i = 0; i < setter.size(); i++) {
      VanillaHttpProblemDetail one = new VanillaHttpProblemDetail();
      VanillaHttpProblemDetail other = new VanillaHttpProblemDetail();

      setter.forEach(consumer -> consumer.accept(one));
      setter.forEach(consumer -> consumer.accept(other));

      if (i == currentRound) {
        otherValueSetter.get(currentRound).accept(other);
      }

      assertThat(one).isNotEqualTo(other).doesNotHaveSameHashCodeAs(other);

      currentRound++;
    }
  }

  @ArgumentsSource(ArgumentSourceForValidValues.class)
  @ParameterizedTest
  void equalsWorksForRHSIsNull(String instance, String type, Integer status, String title, String detail) {
    List<Consumer<VanillaHttpProblemDetail>> setter = List.of((VanillaHttpProblemDetail details) -> details.setInstance(instance),
                                                               (VanillaHttpProblemDetail details) -> details.setType(type),
                                                               (VanillaHttpProblemDetail details) -> details.setStatus(status),
                                                               (VanillaHttpProblemDetail details) -> details.setTitle(title),
                                                               (VanillaHttpProblemDetail details) -> details.setDetail(detail));

    VanillaHttpProblemDetail problem = new VanillaHttpProblemDetail();

    assertThat(problem).isNotEqualTo(null);
  }

  @ArgumentsSource(ArgumentSourceForValidValues.class)
  @ParameterizedTest
  void equalsWorksForSameInstanceOnRHSl(URI instance, String type, Integer status, String title, String detail) {
    List<Consumer<VanillaHttpProblemDetail>> setter = List.of((VanillaHttpProblemDetail details) -> details.setInstance(instance),
                                                               (VanillaHttpProblemDetail details) -> details.setType(type),
                                                               (VanillaHttpProblemDetail details) -> details.setStatus(status),
                                                               (VanillaHttpProblemDetail details) -> details.setTitle(title),
                                                               (VanillaHttpProblemDetail details) -> details.setDetail(detail));

    VanillaHttpProblemDetail problem = new VanillaHttpProblemDetail();

    assertThat(problem).isEqualTo(problem);
  }



  private static class ArgumentSourceForValidValues implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      return Stream.of(Arguments.of("about:instance", "about:type", 200, "title", "detail"));
    }
  }
}
