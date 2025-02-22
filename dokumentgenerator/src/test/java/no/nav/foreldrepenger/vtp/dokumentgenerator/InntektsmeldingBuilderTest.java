package no.nav.foreldrepenger.vtp.dokumentgenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders.ArbeidsforholdBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders.InntektsmeldingBuilder;
import no.seres.xsd.nav.inntektsmelding_m._20181211.InntektsmeldingM;
import org.junit.Assert;
import org.junit.Test;

import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingErketype;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.DelvisFravaer;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Omsorgspenger;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Periode;

public class InntektsmeldingBuilderTest{

    @Test
    public void inntektsmeldingGeneratorTest(){
        InntektsmeldingErketype inntektsmeldingErketype = new InntektsmeldingErketype();

        String xml =  inntektsmeldingErketype.standardInntektsmelding("123123", "13213123");

        Assert.assertTrue(xml.toLowerCase().contains("virksomhetsnummer"));
    }
    @Test
    public void IMtest(){
        InntektsmeldingBuilder inntektsmeldingBuilder = new InntektsmeldingBuilder()
                .medBeregnetInntekt(BigDecimal.valueOf(3000))
                .medArbeidstakerFNR("111111")
                .medStartdatoForeldrepengerperiodenFOM(LocalDate.now().plusWeeks(2))
                .medArbeidsgiver("13213123", "3333333")
                .medArbeidstakerFNR("121313212313")
                .medYtelse(YtelseKodeliste.FORELDREPENGER)
                .medAarsakTilInnsending(ÅrsakInnsendingKodeliste.NY);
        InntektsmeldingM im = inntektsmeldingBuilder.build();
        System.out.println("end");
    }

    @Test
    public void inntektsmeldingOmsorgspengerTest(){
        no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder inntektsmeldingBuilder = new no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder("123", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY, "12345678", LocalDate.now());
        String inntektesmeldingXML = inntektsmeldingBuilder.createInntektesmeldingXML();
        Assert.assertNotEquals(0, inntektesmeldingXML.length());

        List<DelvisFravaer> delvisFravaers = new ArrayList<DelvisFravaer>();

        DelvisFravaer df1 = inntektsmeldingBuilder.createDelvisFravaer(
                LocalDate.now().minusWeeks(3),
                BigDecimal.TEN
        );

        delvisFravaers.add(df1);

        Omsorgspenger omsorgspenger = inntektsmeldingBuilder.createOmsorgspenger(
                inntektsmeldingBuilder.createDelvisFravaersListeForOmsorgspenger(delvisFravaers),
                null,
                true
        );

        inntektsmeldingBuilder.setOmsorgspenger(omsorgspenger);

        String inntektesmeldingXML2 = inntektsmeldingBuilder.createInntektesmeldingXML();

        Assert.assertTrue(inntektesmeldingXML2.contains("omsorg"));
        Assert.assertTrue(inntektesmeldingXML2.contains("delvisFravaer"));
        Assert.assertTrue(inntektesmeldingXML2.contains("<timer>10</timer>"));

        Periode fravaersPeriode = inntektsmeldingBuilder.createInntektsmeldingPeriode(LocalDate.now().minusWeeks(3), LocalDate.now().minusWeeks(2));
        List<Periode> fravaersPerioder = new ArrayList<Periode>();
        fravaersPerioder.add(fravaersPeriode);
        omsorgspenger.setFravaersPerioder(inntektsmeldingBuilder.createFravaersPeriodeListeForOmsorgspenger(fravaersPerioder));

        inntektsmeldingBuilder.setOmsorgspenger(omsorgspenger);
        String inntektesmeldingXML3 = inntektsmeldingBuilder.createInntektesmeldingXML();


        Assert.assertTrue(inntektesmeldingXML3.contains("fravaerPeriode"));
    }

    @Test
    public void inntektsmeldingPleiepengerTest(){
        no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder inntektsmeldingBuilder = new no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder("123", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY, "12345678", LocalDate.now());
        String inntektesmeldingXML = inntektsmeldingBuilder.createInntektesmeldingXML();
        Assert.assertNotEquals(0, inntektesmeldingXML.length());

        List<Periode> perioder = new ArrayList<>();

        Periode pleiePeriode = inntektsmeldingBuilder.createInntektsmeldingPeriode(LocalDate.now().minusWeeks(3), LocalDate.now().minusWeeks(2));
        perioder.add(pleiePeriode);

        inntektsmeldingBuilder.setPleiepengerPeriodeListe(
                inntektsmeldingBuilder.createPleiepenger(perioder)
        );

        String inntektesmeldingXML1 = inntektsmeldingBuilder.createInntektesmeldingXML();

        Assert.assertTrue(inntektesmeldingXML1.contains("pleiepengerPerioder"));
    }


    @Test
    public void arbeidsgiverErPrivatTestr(){
        no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder inntektsmeldingBuilder = new no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder("123", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY, "12345678", LocalDate.now());
        String inntektesmeldingXML = inntektsmeldingBuilder.createInntektesmeldingXML();
        Assert.assertNotEquals(0, inntektesmeldingXML.length());


        inntektsmeldingBuilder.setArbeidsgiverPrivat(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createArbeidsgiverPrivat("123"));

        String inntektesmeldingXML1 = inntektsmeldingBuilder.createInntektesmeldingXML();
        Assert.assertTrue(inntektesmeldingXML1.contains("arbeidsgiverFnr"));

    }

}
