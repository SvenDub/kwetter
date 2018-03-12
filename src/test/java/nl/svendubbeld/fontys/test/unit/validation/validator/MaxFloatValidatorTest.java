package nl.svendubbeld.fontys.test.unit.validation.validator;

import nl.svendubbeld.fontys.validation.constraints.MaxFloat;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MaxFloatValidatorTest extends ValidatorTest{

    @Test
    public void testNull() {
        Container container = new Container(null);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testGreater() {
        Container container = new Container(50.51f);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(not(empty())));
    }

    @Test
    public void testEqual() {
        Container container = new Container(50.5f);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testLower() {
        Container container = new Container(50.49f);

        Set<ConstraintViolation<Container>> violations = getValidator().validate(container);

        assertThat(violations, is(empty()));
    }

    private class Container {

        @MaxFloat(50.5)
        private Float value;

        public Container(Float value) {
            this.value = value;
        }
    }

}