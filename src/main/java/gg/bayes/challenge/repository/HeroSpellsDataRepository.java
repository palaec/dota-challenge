package gg.bayes.challenge.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.rest.model.HeroSpellsData;

public interface HeroSpellsDataRepository extends JpaRepository<HeroSpellsData, Long> {

	@Query(value = "SELECT new  gg.bayes.challenge.rest.model.HeroSpells(spell,CAST(COUNT(spell) AS int)) FROM HeroSpellsData WHERE hero =?1 AND matchId = ?2 GROUP BY spell")
	List<HeroSpells> findSpellAndSpellsCountByHeroAndMatchId(String hero , Long matchId);
	
}
