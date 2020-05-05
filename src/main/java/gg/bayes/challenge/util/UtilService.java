package gg.bayes.challenge.util;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import gg.bayes.challenge.rest.model.HeroDamageData;
import gg.bayes.challenge.rest.model.HeroItemsData;
import gg.bayes.challenge.rest.model.HeroKillsData;
import gg.bayes.challenge.rest.model.HeroSpellsData;


@Component
public class UtilService {

	public long parseTimeToMillSec(String period) {
		Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");
		Matcher matcher = pattern.matcher(period);
		if (matcher.matches()) {
			return Long.parseLong(matcher.group(1)) * 3600000L + Long.parseLong(matcher.group(2)) * 60000
					+ Long.parseLong(matcher.group(3)) * 1000 + Long.parseLong(matcher.group(4));
		} else {
			throw new IllegalArgumentException("Invalid format " + period);
		}
	}

	public List<HeroItemsData> getHeroItemsData(Long matchId, String line, List<HeroItemsData> hList) {
		String data[] = line.split(" ");
		if (data[1].contains("npc_dota_hero_")) { // Adding only for heros with npc_dota_hero_
			HeroItemsData heroItems = new HeroItemsData();
			heroItems.setTimestamp(parseTimeToMillSec(data[0].replaceAll("\\[|\\]", "")));
			heroItems.setMatchId(matchId);
			heroItems.setHero(data[1].replace("npc_dota_hero_", ""));
			heroItems.setItem(data[4].replace("item_", ""));
			hList.add(heroItems);
		}
		return hList;
	}

	public Long getMatchId(String payload) throws NumberFormatException {
		return Long.parseLong(payload.substring(payload.indexOf("_") + 1, payload.indexOf(".txt")));
	}

	public List<HeroKillsData> getHeroKillsData(Long matchId, String line, List<HeroKillsData> kList) {
		String data[] = line.split(" ");
		if (data[5].contains("npc_dota_hero_")) { // Adding only for heros with npc_dota_hero_ and considering target any type
			HeroKillsData heroKills = new HeroKillsData();
			heroKills.setHero(data[5].replace("npc_dota_hero_", ""));
			heroKills.setMatchId(matchId);
			heroKills.setTarget(data[1].replace("npc_dota_hero_", ""));
			heroKills.setTimestamp(parseTimeToMillSec(data[0].replaceAll("\\[|\\]", "")));
			kList.add(heroKills);
		}
		return kList;
	}

	public List<HeroSpellsData> getHeroSpellsData(Long matchId, String line, List<HeroSpellsData> sList) {
		String data[] = line.split(" ");
		if (data[1].contains("npc_dota_hero_")) { // Adding only for heros with npc_dota_hero_ and considering target any type
			HeroSpellsData heroSpells = new HeroSpellsData();
			heroSpells.setTimestamp(parseTimeToMillSec(data[0].replaceAll("\\[|\\]", "")));
			heroSpells.setMatchId(matchId);
			heroSpells.setHero(data[1].replace("npc_dota_hero_", ""));
			heroSpells.setSpell(data[4]);
			heroSpells.setLevel(Integer.parseInt(data[6].replaceAll("\\)", "")));
			heroSpells.setTarget(data[8].replace("npc_dota_hero_", ""));
			sList.add(heroSpells);
		}
		return sList;
	}

	public List<HeroDamageData> getHeroDamageData(Long matchId, String line, List<HeroDamageData> dList) {
		String data[] = line.split(" ");
		if (data[1].contains("npc_dota_hero_") && data[3].contains("npc_dota_hero_")) { // Adding only for heros with npc_dota_hero_ and target with npc_dota_hero_
			HeroDamageData heroDamage = new HeroDamageData();
			heroDamage.setTimestamp(parseTimeToMillSec(data[0].replaceAll("\\[|\\]", "")));
			heroDamage.setMatchId(matchId);
			heroDamage.setHero(data[1].replace("npc_dota_hero_", ""));
			heroDamage.setDamage(Integer.parseInt(data[7]));
			heroDamage.setTarget(data[3].replace("npc_dota_hero_", ""));
			heroDamage.setAttackWith(data[5]);
			if(data[5].equals("dota_unknown")) {
				heroDamage.setAttackType("Normal");
			}else {
				heroDamage.setAttackType("ItemOrSpell");
			}
			dList.add(heroDamage);
		}
		return dList;
	}
}
