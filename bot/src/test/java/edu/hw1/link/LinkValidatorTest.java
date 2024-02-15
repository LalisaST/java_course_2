package edu.hw1.link;

import edu.java.bot.link.GitHubLink;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkValidator;
import edu.java.bot.link.StackOverflowLink;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkValidatorTest {
    LinkValidator linkValidator;

    @BeforeEach
    public void setup() {
        List<Link> listLinks = Arrays.asList(new StackOverflowLink(), new GitHubLink());
        linkValidator = new LinkValidator(listLinks);
    }

    @Test
    @DisplayName("проверка на отслеживаемый хост")
    void checkingTraceableHost() {
        URI url1 = URI.create("https://github.com/LalisaST?tab=repositories");
        URI url2 = URI.create(
            "https://stackoverflow.com/questions/78003027/how-can-i-create-a-complex-web-ui-section-similar-to-the-one-in-figma-using-cs");
        assertThat(linkValidator.isValid(url1)).isEqualTo(true);
        assertThat(linkValidator.isValid(url2)).isEqualTo(true);
    }

    @Test
    @DisplayName("проверка на неотслеживаемый хост")
    void checkingUntraceableHost() {
        URI url1 = URI.create(
            "https://www.tinkoff.ru/cards/debit-cards/tinkoff-black/form/payments-and-transfers/?utm_source=yandex&utm_medium=ctx.cpc&utm_campaign=debit.black_pip_0124_tb_search_ya_brand.general.conv_aon_0423_rus&utm_term=тинькофф&utm_content=pid%7C44236672605%7Cretid%7C44236672605%7Ccid%7C86246291%7Cgid%7C5173117400%7Caid%7C13938003676%7Cpostype%7Cpremium%7Cpos%7C1%7Csrc%7Cnone%7Cdvc%7Cdesktop%7Cregionid%7C192%7Ccorid%7C23350863&yclid=10066462238335565823");
        assertThat(linkValidator.isValid(url1)).isEqualTo(false);
    }
}
