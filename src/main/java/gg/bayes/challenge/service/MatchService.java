package gg.bayes.challenge.service;

import java.util.List;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

public interface MatchService {
	
    Long ingestMatch(String payload);

	List<HeroItems> findItemAndTimestampByMatchIdAndHero(Long matchId, String heroName);

	List<HeroKills> findHeroAndKillsCountByMatchId(Long matchId);

	List<HeroSpells> findSpellAndSpellsCountByHeroAndMatchId(String heroName, Long matchId);

	List<HeroDamage> findtargetAndDamageCountByHeroAndMatchId(String heroName, Long matchId);

}
