package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.autotest.scenario.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.autotest.scenario.personopplysning.AnnenPartModell;
import no.nav.foreldrepenger.vtp.autotest.scenario.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.autotest.scenario.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.autotest.scenario.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.autotest.scenario.util.VariabelContainer;

import java.util.*;
import java.util.stream.Stream;

public class Personopplysninger {

    @JsonProperty("søker")
    private SøkerModell søker;

    @JsonProperty("annenPart")
    private AnnenPartModell annenPart;

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("familierelasjoner")
    private List<FamilierelasjonModell> familierelasjoner = new ArrayList<>();

    @JsonProperty("familierelasjonerAnnenPart")
    private List<FamilierelasjonModell> familierelasjonerAnnenPart = new ArrayList<>();

    @JsonProperty("familierelasjonerBarn")
    private List<FamilierelasjonModell> familierelasjonerBarn = new ArrayList<>();

    /**
     * identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet.
     * Deler VirksomhetIndeks for et helt scenario for å veksle lokale identer inn i fnr el.
     */
    @JacksonInject
    private LokalIdentIndeks identer;

    @JacksonInject
    private VariabelContainer vars;

    public Personopplysninger(SøkerModell søker) {
        this.søker = søker;
    }

    public Personopplysninger(SøkerModell søker, AnnenPartModell annenPart) {
        this.søker = søker;
        this.annenPart = annenPart;
    }

    Personopplysninger() {
    }


    public AnnenPartModell getAnnenPart() {
        return annenPart;
    }

    public Collection<FamilierelasjonModell> getFamilierelasjoner() {
        return Collections.unmodifiableList(familierelasjoner);
    }

    public Collection<FamilierelasjonModell> getFamilierelasjonerForAnnenPart() {
        return Collections.unmodifiableList(familierelasjonerAnnenPart);
    }

    public Collection<FamilierelasjonModell> getFamilierelasjonerForBarnet() {
        return Collections.unmodifiableList(familierelasjonerBarn);
    }

    public Stream<FamilierelasjonModell> getFamilierelasjoner(FamilierelasjonModell.Rolle rolle) {
        return getFamilierelasjoner().stream().filter(f -> rolle.equals(f.getRolle()));
    }

    public SøkerModell getSøker() {
        return søker;
    }

    public void leggTil(FamilierelasjonModell rel) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");
        rel.getTil().setIdenter(identer);
        this.familierelasjoner.add(rel);
    }

    public void leggTilBarn(BarnModell barn) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");
        barn.setIdenter(identer);
        this.familierelasjoner.add(new FamilierelasjonModell(FamilierelasjonModell.Rolle.BARN, barn));
    }

    public void setIdenter(LokalIdentIndeks identer) {
        this.identer = identer;
        this.søker.setIdenter(identer);
        if (this.annenPart != null) {
            this.annenPart.setIdenter(identer);
        }
    }
}
