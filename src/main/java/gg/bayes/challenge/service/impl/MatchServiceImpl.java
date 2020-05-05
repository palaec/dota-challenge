package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.exception.MatchServiceException;
import gg.bayes.challenge.repository.HeroDamageDataRepository;
import gg.bayes.challenge.repository.HeroItemsDataRepository;
import gg.bayes.challenge.repository.HeroKillsDataRepository;
import gg.bayes.challenge.repository.HeroSpellsDataRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroDamageData;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroItemsData;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroKillsData;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.rest.model.HeroSpellsData;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.util.UtilService;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

	private UtilService utilService;
	private HeroItemsDataRepository heroItemsDataRepo;
	private HeroKillsDataRepository heroKillsDataRepo;
	private HeroSpellsDataRepository heroSpellsDataRepo;
	private HeroDamageDataRepository heroDamageDataRepo;

	@Autowired
	public MatchServiceImpl(UtilService utilService, HeroItemsDataRepository heroItemsDataRepo,
			HeroKillsDataRepository heroKillsDataRepo, HeroSpellsDataRepository heroSpellsDataRepo,
			HeroDamageDataRepository heroDamageDataRepo) {
		this.utilService = utilService;
		this.heroItemsDataRepo = heroItemsDataRepo;
		this.heroKillsDataRepo = heroKillsDataRepo;
		this.heroSpellsDataRepo = heroSpellsDataRepo;
		this.heroDamageDataRepo = heroDamageDataRepo;
	}

	/**
	 * This method is used to read the events for file and save to database.
	 */
	@Override
	public Long ingestMatch(String payload) {
		log.info("START-- ingest Match ", payload);
		List<HeroItemsData> hList = new ArrayList<>();
		List<HeroKillsData> kList = new ArrayList<>();
		List<HeroSpellsData> sList = new ArrayList<>();
		List<HeroDamageData> dList = new ArrayList<>();
		String line;
		Long matchId = null;
		try {
			matchId = utilService.getMatchId(payload);
		}catch(NumberFormatException ex) {
			throw new MatchServiceException("Please enter file name as combatlog_{{any integer}}.text like combatlog_1.txt");
		}
		if (heroItemsDataRepo.existsByMatchId(matchId)) { // Validation to check if the file is already saved
			throw new MatchServiceException("This file already Saved");
		}
		log.info("Reading from file and creating Event objects");
		try (BufferedReader br = new BufferedReader(new FileReader(payload))) {
			while ((line = br.readLine()) != null) {
				if (line.contains("buys item")) {
					hList = utilService.getHeroItemsData(matchId, line, hList);
				} else if (line.contains("killed by")) {
					kList = utilService.getHeroKillsData(matchId, line, kList);
				} else if (line.contains("casts")) {
					sList = utilService.getHeroSpellsData(matchId, line, sList);
				} else if (line.contains("hits")) {
					dList = utilService.getHeroDamageData(matchId, line, dList);
				}
			}
			log.info("Event Objects created");
		} catch (IOException e) {
			throw new MatchServiceException(e.getMessage());
		}

		ingestMatchData(hList, kList, sList, dList);
		
		log.info("END-- ingest Match");
		return matchId;
	}

	@Transactional
	public Boolean ingestMatchData(List<HeroItemsData> hList, List<HeroKillsData> kList, List<HeroSpellsData> sList,
			List<HeroDamageData> dList) {
		log.info("START-- Saving Events Objects to Database to corrosponding tables");
		heroItemsDataRepo.saveAll(hList);
		heroKillsDataRepo.saveAll(kList);
		heroSpellsDataRepo.saveAll(sList);
		heroDamageDataRepo.saveAll(dList);
		log.info("END-- Saving Events Objects to Database");
		return true;
	}

	@Override
	public List<HeroItems> findItemAndTimestampByMatchIdAndHero(Long matchId, String heroName) {
		return heroItemsDataRepo.findItemAndTimestampByMatchIdAndHero(matchId, heroName);
	}

	@Override
	public List<HeroKills> findHeroAndKillsCountByMatchId(Long matchId) {
		return heroKillsDataRepo.findHeroAndKillsCountByMatchId(matchId);
	}

	@Override
	public List<HeroSpells> findSpellAndSpellsCountByHeroAndMatchId(String heroName, Long matchId) {
		return heroSpellsDataRepo.findSpellAndSpellsCountByHeroAndMatchId(heroName, matchId);
	}

	@Override
	public List<HeroDamage> findtargetAndDamageCountByHeroAndMatchId(String heroName, Long matchId) {
		return heroDamageDataRepo.findtargetAndDamageCountByHeroAndMatchId(heroName, matchId);
	}

}
