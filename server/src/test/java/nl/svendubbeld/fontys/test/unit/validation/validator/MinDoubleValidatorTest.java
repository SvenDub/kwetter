package nl.svendubbeld.fontys.test.unit.validation.validator;

import nl.svendubbeld.fontys.validation.constraints.MinFloat;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MinDoubleValidatorTest extends ValidatorTest{

    @Test
    public void testNull() {
        Container container = new Container(null);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testGreater() {
        Container container = new Container(50.51);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testEqual() {
        Container container = new Container(50.5);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testLower() {
        Container container = new Container(50.49);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(not(empty())));
    }

    private class Container {

        @MinFloat(50.5)
        private Double value;

        public Container(Double value) {
            this.value = value;
        }
    }

}