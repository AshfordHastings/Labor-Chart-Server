package labor.Command.User;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.LaborSlot;
import labor.Service.LaborService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Message;

public class GetLaborTimeCommand implements Command {
	
	// !get_labortime 9:00 Tuesday / OR !get_labortime 9:00
	@CommandInfo(name="get_labortime")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		
		List<String> messageParsed = Stream.of(message.getContentDisplay().split(" "))
				.map(str -> new String(str))
				.collect(Collectors.toList());
		
		DayOfWeek dayOfWeek;
		LocalTime time;
		
		if(messageParsed.size() == 2) {
			Calendar calendar = Calendar.getInstance();
			dayOfWeek = DayOfWeek.of(calendar.get(Calendar.DAY_OF_WEEK));
		} else if (messageParsed.size() == 3) {
			dayOfWeek = DayOfWeek.valueOf(messageParsed.get(2).toUpperCase());
		} else {
			discordOutput.sendMessage("Improper amount of arguments for !get_labortime! You must be dumb! ");
			return;
		}
		
		time = LocalTime.parse(messageParsed.get(1));
		
		List<LaborSlot> laborSlotQueryReturn = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, time.toString());
		laborSlotQueryReturn = laborSlotQueryReturn.stream()
			.filter(laborSlot -> laborSlot.getMember() != null)
			.collect(Collectors.toList());
		
		StringBuilder returnString = new StringBuilder();
		returnString.append("Labor Slots and Members on " + dayOfWeek + " at " + time + ": \n");
			
		if(!(laborSlotQueryReturn.size() > 0)) {
			returnString.append("There are no labor slots with active coopers at that time.");
		} else {
			for(LaborSlot laborSlot : laborSlotQueryReturn) {
				returnString.append(laborSlot.getMember().getDiscordTag() + " - " + laborSlot.getPosition().getName() + '\n');
			}
		}
		discordOutput.sendMessage(returnString.toString());
	}

	
}
