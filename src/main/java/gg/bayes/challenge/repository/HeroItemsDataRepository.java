package gg.bayes.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroItemsData;

@Repository
public interface HeroItemsDataRepository extends JpaRepository<HeroItemsData, Long> {

	List<HeroItems> findItemAndTimestampByMatchIdAndHero(Long matchId, String heroName);

	Boolean existsByMatchId(Long matchId);

}
