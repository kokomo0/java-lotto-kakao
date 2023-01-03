import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class LottoTest {

    private static Lottos lottos;

    @BeforeAll
    static void makeLottos() {
        List<Lotto> lottoList = new ArrayList<>();

        int[][] inputs= {
                {8, 21, 23, 41, 42, 43},
                {3, 5, 11, 16, 32, 38},
                {7, 11, 16, 35, 36, 44},
                {1, 8, 11, 31, 41, 42},
                {13, 14, 16, 38, 42, 45},
                {7, 11, 30, 40, 42, 43},
                {2, 13, 22, 32, 38, 45},
                {23, 25, 33, 36, 39, 41},
                {1, 3, 5, 14, 22, 45},
                {5, 9, 38, 41, 43, 44},
                {2, 8, 9, 18, 19, 21},
                {13, 14, 18, 21, 23, 35},
                {17, 21, 29, 37, 42, 45},
                {3, 8, 27, 30, 35, 44},
                {1, 2, 3, 4, 5, 7}
        };

        for(int[] input : inputs) {
            List<Integer> tmp = Arrays.stream(input)
                    .boxed()
                    .collect(Collectors.toList());
            lottoList.add(new Lotto(asLottoNumbers(tmp)));
        }
        lottos = new Lottos(lottoList, lottoList.size());
    }

    @Test
    void 로또번호_6개를_발급한다() {
        Lotto lotto = LottoFactory.createLotto();
        assertThat(lotto.getLottoNumbers()).hasSize(6);
    }

    @Test
    void 로또번호는_1이상_45이하이다() {
        Lotto lotto = LottoFactory.createLotto();
        for (LottoNumber lottoNumber : lotto.getLottoNumbers()) {
            assertThat(lottoNumber.getNumber()).isBetween(1, 45);
        }
    }

    @Test
    void 로또번호는_중복되지_않는다() {
        Lotto lotto = LottoFactory.createLotto();
        assertThat(lotto.getLottoNumbers()).doesNotHaveDuplicates();
    }

    @ParameterizedTest
    @ValueSource(ints = {10000, 20000, 5000, 1000, 3000})
    void  로또_구입_금액에_해당하는_로또를_발급한다(final int amount) {
        Lottos lottos = LottoFactory.createLottos(amount);
        assertThat(lottos.getLottoList()).hasSize(amount / LottoFactory.getLottoPrice());
    }

    static List<LottoNumber> asLottoNumbers (List<Integer> lottoNumbers) {
        List<LottoNumber> result = new ArrayList<>();
        for (int number : lottoNumbers)
            result.add(LottoNumber.getLottoNumber(number));
        return result;
    }

    static Stream<Arguments> lottoData() {
        return Stream.of(
                Arguments.of(asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), 6),
                Arguments.of(asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), asLottoNumbers(Arrays.asList(2, 4, 6, 8, 10, 20)), 3)
        );
    }

    @ParameterizedTest
    @MethodSource("lottoData")
    void 로또_번호가_몇_개_일치하는지_계산한다(List<LottoNumber> lottoNumbers, List<LottoNumber> winNumbers, int answer) {
        Lotto lotto = new Lotto(lottoNumbers);
        assertThat(lotto.getMatchCount(winNumbers)).isEqualTo(answer);
    }

    @ParameterizedTest
    @CsvSource({"0,false,0","1,false,0","2,false,0","3,false,5000","4,false,50000","5,true,30000000","5,false,1500000","6,true,2000000000"})
    void 로또의_당첨금액을_계산한다(int matchCount, boolean isBonusMatch, int lotteryAmount) {
        assertThat(Lotto.getLotteryAmount(matchCount, isBonusMatch)).isEqualTo(lotteryAmount);
    }

    static Stream<Arguments> lottoData2() {
        return Stream.of(
                Arguments.of(asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), 6),
                Arguments.of(asLottoNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)), asLottoNumbers(Arrays.asList(2, 4, 6, 8, 10, 20)), 3)
        );
    }

    @Test
    void 총_수익률을_계산하여_출력한다() {
        List<LottoNumber> winNumbers = asLottoNumbers(Arrays.asList(1,2,3,4,5,6));
        WinLottoNumbers winLottoNumbers = new WinLottoNumbers();

        winLottoNumbers.setWinNumbers(winNumbers);
        winLottoNumbers.setBonusNumber(LottoNumber.getLottoNumber(7));
        assertThat(Math.floor(lottos.getTotalLotteryRate(lottos.getTotalLotteryAmount(winLottoNumbers), 15000.0) * 100)).isEqualTo(200033);
    }
}