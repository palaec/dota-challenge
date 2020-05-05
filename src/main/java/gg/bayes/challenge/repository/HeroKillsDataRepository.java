package gg.bayes.challenge.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroKillsData;

public interface HeroKillsDataRepository extends JpaRepository<HeroKillsData, Long> {

	@Query(value = "SELECT new  gg.bayes.challenge.rest.model.HeroKills(hero,CAST(COUNT(hero) AS int)) FROM HeroKillsData WHERE matchId = ?1 GROUP BY hero")
	List<HeroKills> findHeroAndKillsCountByMatchId(Long matchId);


}
