package gg.bayes.challenge;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import gg.bayes.challenge.rest.model.HeroDamageData;
import gg.bayes.challenge.rest.model.HeroItemsData;
import gg.bayes.challenge.rest.model.HeroKillsData;
import gg.bayes.challenge.rest.model.HeroSpellsData;
import gg.bayes.challenge.util.UtilService;
import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UtilServiceTests {
	
	@InjectMocks
	private UtilService utilService;
	
    @Test
    void getHeroItemsData_test() {
    	List<HeroItemsData> hList = new ArrayList<HeroItemsData>();    	
    	Long matchId = 1L;
    	String line = "[00:09:01.922] npc_dota_hero_bloodseeker buys item item_branches";
		Assert.assertEquals(1, utilService.getHeroItemsData(matchId, line, hList).size());
		Assert.assertEquals("branches", utilService.getHeroItemsData(matchId, line, hList).get(0).getItem());
		Assert.assertEquals("bloodseeker", utilService.getHeroItemsData(matchId, line, hList).get(0).getHero());
    }
    
    @Test
    void getHeroKillsData_test() {
    	List<HeroKillsData> hList = new ArrayList<HeroKillsData>();    	
    	Long matchId = 1L;
    	String line = "[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars";
		Assert.assertEquals(1, utilService.getHeroKillsData(matchId, line, hList).size());
		Assert.assertEquals("snapfire", utilService.getHeroKillsData(matchId, line, hList).get(0).getTarget());
		Assert.assertEquals("mars", utilService.getHeroKillsData(matchId, line, hList).get(0).getHero());
    }
    
    @Test
    void getHeroSpellsData_test() {
    	List<HeroSpellsData> hList = new ArrayList<HeroSpellsData>();    	
    	Long matchId = 1L;
    	String line = "[00:29:05.928] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_puck";
		Assert.assertEquals(1, utilService.getHeroSpellsData(matchId, line, hList).size());
		Assert.assertEquals("puck", utilService.getHeroSpellsData(matchId, line, hList).get(0).getTarget());
		Assert.assertEquals("rubick", utilService.getHeroSpellsData(matchId, line, hList).get(0).getHero());
		Assert.assertEquals(1, utilService.getHeroSpellsData(matchId, line, hList).get(0).getLevel(),0);
    }
    
    @Test
    void getHeroDamageData_test() {
    	List<HeroDamageData> hList = new ArrayList<HeroDamageData>();    	
    	Long matchId = 1L;
    	String line = "[00:29:06.328] npc_dota_hero_rubick hits npc_dota_hero_puck with snapfire_spit_creep for 43 damage (270->227)";
		Assert.assertEquals(1, utilService.getHeroDamageData(matchId, line, hList).size());
		Assert.assertEquals("puck", utilService.getHeroDamageData(matchId, line, hList).get(0).getTarget());
		Assert.assertEquals("rubick", utilService.getHeroDamageData(matchId, line, hList).get(0).getHero());
		Assert.assertEquals(43, utilService.getHeroDamageData(matchId, line, hList).get(0).getDamage(),0);
    }
    
    @Test
    void getMatchId_test() {
    	String line = "D:\\workspace-sts-4-4.5.1.RELEASE\\bayes-java-code-challenge-1\\data\\combatlog_1.txt";
		Assert.assertEquals(1, utilService.getMatchId(line),0);
    }
    
    @Test
    void parseTimeToMillSec_test() {
    	String time = "00:29:06.328";
		Assert.assertEquals(1746328, utilService.parseTimeToMillSec(time),0);
    }
}
