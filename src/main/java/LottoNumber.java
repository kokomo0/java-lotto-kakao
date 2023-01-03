//1~45

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LottoNumber {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;

    private static final Map<Integer, LottoNumber> lottoNumbers = new HashMap<>();

    private int number;

    static {
        IntStream.range(MIN_NUMBER, MAX_NUMBER)
                .forEach(number -> lottoNumbers.put(number, new LottoNumber(number)));
    }

    private LottoNumber(int number) {
        this.number = number;
    }

    public static LottoNumber getLottoNumber(int number) {
        return lottoNumbers.get(number);
    }

    public static List<LottoNumber> getLottoNumbers() {
        return new ArrayList<>(lottoNumbers.values());
    }

    public int getNumber() {
        return number;
    }


}
