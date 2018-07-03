import org.junit.Test;

import java.nio.charset.CharacterCodingException;

public class Run_Test {

    @Test
    public void getNumber() throws CharacterCodingException {
        System.out.println(Run.getNumber("Приблизна кількість результатів: 3 870 (0,49 сек.) "));
    }
}