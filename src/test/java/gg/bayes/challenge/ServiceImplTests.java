package gg.bayes.challenge;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
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
import gg.bayes.challenge.service.impl.MatchServiceImpl;
import gg.bayes.challenge.util.UtilService;
import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ServiceImplTests {

	@Mock
	private UtilService utilMethods;
	@Mock
	private HeroItemsDataRepository heroItemsDataRepo;
	@Mock
	private HeroKillsDataRepository heroKillsDataRepo;
	@Mock
	private HeroSpellsDataRepository heroSpellsDataRepo;
	@Mock
	private HeroDamageDataRepository heroDamageDataRepo;
	
	@InjectMocks
	private MatchServiceImpl matchServiceImpl;
	
    @Test
    void findItemAndTimestampByMatchIdAndHero_test() {
    	List<HeroItems> list = new ArrayList<>();
    	list.add(new HeroItems("clarity",526693L));
    	list.add(new HeroItems("ogre_axe",833951L));    	
    	when(heroItemsDataRepo.findItemAndTimestampByMatchIdAndHero(Mockito.anyLong(), Mockito.anyString())).thenReturn(list);
		Assert.assertEquals(2, matchServiceImpl.findItemAndTimestampByMatchIdAndHero(1L, "snapfire").size());
		Assert.assertEquals("clarity", matchServiceImpl.findItemAndTimestampByMatchIdAndHero(1L, "snapfire").get(0).getItem());
    }

    @Test
    void findHeroAndKillsCountByMatchId_test() {
    	List<HeroKills> list1 = new ArrayList<>();
    	list1.add(new HeroKills("centaur",79));
    	list1.add(new HeroKills("earthshaker",24));    	
    	when(heroKillsDataRepo.findHeroAndKillsCountByMatchId(Mockito.anyLong())).thenReturn(list1);
    	Assert.assertEquals(2, matchServiceImpl.findHeroAndKillsCountByMatchId(1L).size());
		Assert.assertEquals("centaur", matchServiceImpl.findHeroAndKillsCountByMatchId(1L).get(0).getHero());
    }

    @Test
    void findSpellAndSpellsCountByHeroAndMatchId_test() {
    	List<HeroSpells> list1 = new ArrayList<>();
    	list1.add(new HeroSpells("snapfire_firesnap_cookie",17));
    	list1.add(new HeroSpells("snapfire_gobble_up",13));    	
    	when(heroSpellsDataRepo.findSpellAndSpellsCountByHeroAndMatchId(Mockito.anyString(), Mockito.anyLong())).thenReturn(list1);
    	Assert.assertEquals(2, matchServiceImpl.findSpellAndSpellsCountByHeroAndMatchId("mars",2L).size());
		Assert.assertEquals("snapfire_firesnap_cookie", matchServiceImpl.findSpellAndSpellsCountByHeroAndMatchId("snapfire",1L).get(0).getSpell());
    }
    
    @Test
    void findtargetAndDamageCountByHeroAndMatchId_test() {
    	List<HeroDamage> list1 = new ArrayList<>();
    	list1.add(new HeroDamage("centaur",23,4024));
    	list1.add(new HeroDamage("gyrocopter",26,3141));    	
    	when(heroDamageDataRepo.findtargetAndDamageCountByHeroAndMatchId(Mockito.anyString(), Mockito.anyLong())).thenReturn(list1);
    	Assert.assertEquals(2, matchServiceImpl.findtargetAndDamageCountByHeroAndMatchId("mars",2L).size());
		Assert.assertEquals("centaur", matchServiceImpl.findtargetAndDamageCountByHeroAndMatchId("mars",2L).get(0).getTarget());
    }
    
    @Test
    void ingestMatchData_test() {  	
    	List<HeroDamageData> dList = new ArrayList<>();
    	List<HeroItemsData> hList = new ArrayList<>();
		List<HeroKillsData> kList = new ArrayList<>();
		List<HeroSpellsData> sList = new ArrayList<>();
    	when(heroDamageDataRepo.saveAll(dList)).thenReturn(dList);
    	when(heroItemsDataRepo.saveAll(hList)).thenReturn(hList);
    	when(heroKillsDataRepo.saveAll(kList)).thenReturn(kList);
    	when(heroSpellsDataRepo.saveAll(sList)).thenReturn(sList);
    	Assert.assertTrue(matchServiceImpl.ingestMatchData(hList, kList, sList, dList));		
    }
    
    @Test
    void ingestMatch_test() throws IOException {      	
    	MatchServiceImpl spyMatchServiceImpl = Mockito.spy(matchServiceImpl);
    	List<HeroDamageData> dList = new ArrayList<>();
    	List<HeroItemsData> hList = new ArrayList<>();
		List<HeroKillsData> kList = new ArrayList<>();
		List<HeroSpellsData> sList = new ArrayList<>();
		when(utilMethods.getMatchId(Mockito.anyString())).thenReturn(1L);
		when(heroItemsDataRepo.existsByMatchId(Mockito.anyLong())).thenReturn(false);
		Mockito.doReturn(true).when(spyMatchServiceImpl).ingestMatchData(hList, kList, sList, dList); 
    	Assert.assertEquals(1L,matchServiceImpl.ingestMatch("src/test/resources/combatlog_1.txt"),0);		
    }
    
    @Test
    void ingestMatch_ifFileAlreadySaved_test() throws IOException {      	
		when(utilMethods.getMatchId(Mockito.anyString())).thenReturn(1L);
        when(heroItemsDataRepo.existsByMatchId(Mockito.anyLong())).thenReturn(true);
        Exception exception = assertThrows(MatchServiceException.class, () -> {
        	matchServiceImpl.ingestMatch("src/test/resources/combatlog_1.txt");
	    });
		Assert.assertEquals("This file already Saved",exception.getMessage());	
    	
    }
    
}
