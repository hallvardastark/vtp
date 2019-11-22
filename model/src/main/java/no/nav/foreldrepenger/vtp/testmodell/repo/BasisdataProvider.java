package no.nav.foreldrepenger.vtp.testmodell.repo;

import no.nav.foreldrepenger.vtp.autotest.scenario.identer.IdentGenerator;
import no.nav.foreldrepenger.vtp.autotest.scenario.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.VirksomhetIndeks;

public interface BasisdataProvider {

    VirksomhetIndeks getVirksomhetIndeks();

    EnheterIndeks getEnheterIndeks();

    AdresseIndeks getAdresseIndeks();

    /** Genererer nye personidenter. */
    IdentGenerator getIdentGenerator();


}
