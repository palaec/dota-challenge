package gg.bayes.challenge.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroDamageData;

public interface HeroDamageDataRepository extends JpaRepository<HeroDamageData, Long> {

	@Query(value = "SELECT new  gg.bayes.challenge.rest.model.HeroDamage(target,CAST(COUNT(target) AS int),CAST(SUM(damage) AS int) ) FROM HeroDamageData WHERE hero =?1 AND matchId = ?2 GROUP BY target")
	List<HeroDamage> findtargetAndDamageCountByHeroAndMatchId(String hero , Long matchId);
	
}
