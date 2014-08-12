/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flex_client.resources {
	import mx.resources.ResourceManager;
	
	[ResourceBundle("org_flowerplatform_resources")]
	public class Resources {
		
		public static function getMessage(messageId:String, params:Array = null):String {
			return ResourceManager.getInstance().getString("org_flowerplatform_resources", messageId, params);
		}
		
		public static function getResourceUrl(resource:String):String {
			return "servlet/public-resources/org.flowerplatform.resources/" + resource;
		}
		
		//////////////////////////////////
		// Icons
		//////////////////////////////////
		
		// Core
		
		[Embed(source="/core/icon_flower.gif")]
		public static const flowerIcon:Class;
		
		[Embed(source="/core/file.gif")]
		public static const fileIcon:Class;
		
		[Embed(source="/core/disk.png")]
		public static const saveIcon:Class;

		[Embed(source="/core/disk_multiple.png")]
		public static const saveAllIcon:Class;
		
		[Embed(source="/core/refresh_blue.png")]
		public static const reloadIcon:Class;
	
		[Embed(source="/core/external_link.png")]
		public static const externalLinkIcon:Class;
		
		[Embed(source="/core/clipboard-sign-out.png")]
		public static const clipboardIcon:Class;
		
		[Embed(source="/core/open_resource.png")]
		public static const openResourceIcon:Class;
		
		[Embed(source="/core/open.png")]
		public static const openIcon:Class;
		
		[Embed(source="/core/download.png")]
		public static const downloadIcon:Class;
		
		[Embed(source="/core/upload.png")]
		public static const uploadIcon:Class;
		
		[Embed(source="/core/edit.png")]
		public static const editIcon:Class;
		
		[Embed(source="/core/add.png")]
		public static const addIcon:Class;
		
		[Embed(source="/core/cancel_delete.png")]
		public static const deleteIcon:Class;
		
		[Embed(source="/core/arrow_right.png")]
		public static const arrowRightIcon:Class;
		
		[Embed(source="/core/keyboard.png")]
		public static const keyboardIcon:Class;
		
		[Embed(source="/core/table_gear.png")]
		public static const tableGear:Class;
		
		// Properties
		
		[Embed(source="/properties/properties.gif")]
		public static const propertiesIcon:Class;
		
		// CodeSync
		
		[Embed(source="/codesync/Synchronize.gif")]
		public static const synchronizeIcon:Class;
		
		// MindMap
		
		[Embed(source="/mindmap/icons/checkout.gif")]
		public static const mindmap_button_checkout:Class;
		
		[Embed(source="/mindmap/refresh.gif")]
		public static const refreshIcon:Class;
		
		[Embed(source="/mindmap/images.png")]
		public static const imagesIcon:Class;
		
		[Embed(source="/mindmap/remove_first_icon.png")]
		public static const removeFirstIconIcon:Class;
		
		[Embed(source="/mindmap/remove_last_icon.png")]
		public static const removeLastIconIcon:Class;
		
		[Embed(source="/mindmap/icon_trash.png")]
		public static const removeAllIconsIcon:Class;
		
		[Embed(source="/mindmap/icons/help.png")]
		public static const mindmap_helpIcon:Class;
		
		[Embed(source="/mindmap/icons/yes.png")]
		public static const mindmap_yesIcon:Class;
		
		[Embed(source="/mindmap/icons/button_ok.png")]
		public static const mindmap_button_okIcon:Class;
		
		[Embed(source="/mindmap/icons/button_cancel.png")]
		public static const mindmap_button_cancelIcon:Class;
		
		[Embed(source="/mindmap/icons/bookmark.png")]
		public static const mindmap_bookmarkIcon:Class;
		
		[Embed(source="/mindmap/icons/idea.png")]
		public static const mindmap_ideaIcon:Class;
		
		[Embed(source="/mindmap/icons/messagebox_warning.png")]
		public static const mindmap_messagebox_warningIcon:Class;
		
		[Embed(source="/mindmap/icons/stop-sign.png")]
		public static const mindmap_stop_signIcon:Class;
		
		[Embed(source="/mindmap/icons/closed.png")]
		public static const mindmap_closedIcon:Class;
		
		[Embed(source="/mindmap/icons/info.png")]
		public static const mindmap_infoIcon:Class;
		
		[Embed(source="/mindmap/icons/clanbomber.png")]
		public static const mindmap_clanbomberIcon:Class;
		
		[Embed(source="/mindmap/icons/checked.png")]
		public static const mindmap_checkedIcon:Class;
		
		[Embed(source="/mindmap/icons/unchecked.png")]
		public static const mindmap_uncheckedIcon:Class;
		
		[Embed(source="/mindmap/icons/wizard.png")]
		public static const mindmap_wizardIcon:Class;
		
		[Embed(source="/mindmap/icons/gohome.png")]
		public static const mindmap_gohomeIcon:Class;
		
		[Embed(source="/mindmap/icons/knotify.png")]
		public static const mindmap_knotifyIcon:Class;
		
		[Embed(source="/mindmap/icons/password.png")]
		public static const mindmap_passwordIcon:Class;
		
		[Embed(source="/mindmap/icons/pencil.png")]
		public static const mindmap_pencilIcon:Class;
		
		[Embed(source="/mindmap/icons/xmag.png")]
		public static const mindmap_xmagIcon:Class;
		
		[Embed(source="/mindmap/icons/bell.png")]
		public static const mindmap_bellIcon:Class;
		
		[Embed(source="/mindmap/icons/launch.png")]
		public static const mindmap_launchIcon:Class;
		
		[Embed(source="/mindmap/icons/broken-line.png")]
		public static const mindmap_broken_lineIcon:Class;
		
		[Embed(source="/mindmap/icons/stop.png")]
		public static const mindmap_stopIcon:Class;
		
		[Embed(source="/mindmap/icons/prepare.png")]
		public static const mindmap_prepareIcon:Class;
		
		[Embed(source="/mindmap/icons/go.png")]
		public static const mindmap_goIcon:Class;
		
		[Embed(source="/mindmap/icons/very_negative.png")]
		public static const mindmap_very_negativeIcon:Class;
		
		[Embed(source="/mindmap/icons/negative.png")]
		public static const mindmap_negativeIcon:Class;
		
		[Embed(source="/mindmap/icons/neutral.png")]
		public static const mindmap_neutralIcon:Class;
		
		[Embed(source="/mindmap/icons/positive.png")]
		public static const mindmap_positiveIcon:Class;
		
		[Embed(source="/mindmap/icons/very_positive.png")]
		public static const mindmap_very_positiveIcon:Class;
		
		[Embed(source="/mindmap/icons/full-1.png")]
		public static const mindmap_full_1Icon:Class;
		
		[Embed(source="/mindmap/icons/full-2.png")]
		public static const mindmap_full_2Icon:Class;
		
		[Embed(source="/mindmap/icons/full-3.png")]
		public static const mindmap_full_3Icon:Class;
		
		[Embed(source="/mindmap/icons/full-4.png")]
		public static const mindmap_full_4Icon:Class;
		
		[Embed(source="/mindmap/icons/full-5.png")]
		public static const mindmap_full_5Icon:Class;
		
		[Embed(source="/mindmap/icons/full-6.png")]
		public static const mindmap_full_6Icon:Class;
		
		[Embed(source="/mindmap/icons/full-7.png")]
		public static const mindmap_full_7Icon:Class;
		
		[Embed(source="/mindmap/icons/full-8.png")]
		public static const mindmap_full_8Icon:Class;
		
		[Embed(source="/mindmap/icons/full-9.png")]
		public static const mindmap_full_9Icon:Class;
		
		[Embed(source="/mindmap/icons/full-0.png")]
		public static const mindmap_full_0Icon:Class;
		
		[Embed(source="/mindmap/icons/0%.png")]
		public static const mindmap__0PercentIcon:Class;
		
		[Embed(source="/mindmap/icons/25%.png")]
		public static const mindmap__25PercentIcon:Class;
		
		[Embed(source="/mindmap/icons/50%.png")]
		public static const mindmap__50PercentIcon:Class;
		
		[Embed(source="/mindmap/icons/75%.png")]
		public static const mindmap__75PercentIcon:Class;
		
		[Embed(source="/mindmap/icons/100%.png")]
		public static const mindmap__100PercentIcon:Class;
		
		[Embed(source="/mindmap/icons/attach.png")]
		public static const mindmap_attachIcon:Class;
		
		[Embed(source="/mindmap/icons/desktop_new.png")]
		public static const mindmap_desktop_newIcon:Class;
		
		[Embed(source="/mindmap/icons/list.png")]
		public static const mindmap_listIcon:Class;
		
		[Embed(source="/mindmap/icons/edit.png")]
		public static const mindmap_editIcon:Class;
		
		[Embed(source="/mindmap/icons/kaddressbook.png")]
		public static const mindmap_kaddressbookIcon:Class;
		
		[Embed(source="/mindmap/icons/folder.png")]
		public static const mindmap_folderIcon:Class;
		
		[Embed(source="/mindmap/icons/kmail.png")]
		public static const mindmap_kmailIcon:Class;
		
		[Embed(source="/mindmap/icons/Mail.png")]
		public static const mindmap_MailIcon:Class;
		
		[Embed(source="/mindmap/icons/revision.png")]
		public static const mindmap_revisionIcon:Class;
		
		[Embed(source="/mindmap/icons/video.png")]
		public static const mindmap_videoIcon:Class;
		
		[Embed(source="/mindmap/icons/audio.png")]
		public static const mindmap_audioIcon:Class;
		
		[Embed(source="/mindmap/icons/executable.png")]
		public static const mindmap_executableIcon:Class;
		
		[Embed(source="/mindmap/icons/image.png")]
		public static const mindmap_imageIcon:Class;
		
		[Embed(source="/mindmap/icons/internet.png")]
		public static const mindmap_internetIcon:Class;
		
		[Embed(source="/mindmap/icons/internet_warning.png")]
		public static const mindmap_internet_warningIcon:Class;
		
		[Embed(source="/mindmap/icons/mindmap.png")]
		public static const mindmap_mindmapIcon:Class;
		
		[Embed(source="/mindmap/icons/narrative.png")]
		public static const mindmap_narrativeIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-black.png")]
		public static const mindmap_flag_blackIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-blue.png")]
		public static const mindmap_flag_blueIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-green.png")]
		public static const mindmap_flag_greenIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-orange.png")]
		public static const mindmap_flag_orangeIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-pink.png")]
		public static const mindmap_flag_pinkIcon:Class;
		
		[Embed(source="/mindmap/icons/flag.png")]
		public static const mindmap_flagIcon:Class;
		
		[Embed(source="/mindmap/icons/flag-yellow.png")]
		public static const mindmap_flag_yellowIcon:Class;
		
		[Embed(source="/mindmap/icons/clock.png")]
		public static const mindmap_clockIcon:Class;
		
		[Embed(source="/mindmap/icons/clock2.png")]
		public static const mindmap_clock2Icon:Class;
		
		[Embed(source="/mindmap/icons/hourglass.png")]
		public static const mindmap_hourglassIcon:Class;
		
		[Embed(source="/mindmap/icons/calendar.png")]
		public static const mindmap_calendarIcon:Class;
		
		[Embed(source="/mindmap/icons/family.png")]
		public static const mindmap_familyIcon:Class;
		
		[Embed(source="/mindmap/icons/female1.png")]
		public static const mindmap_female1Icon:Class;
		
		[Embed(source="/mindmap/icons/female2.png")]
		public static const mindmap_female2Icon:Class;
		
		[Embed(source="/mindmap/icons/females.png")]
		public static const mindmap_femalesIcon:Class;
		
		[Embed(source="/mindmap/icons/male1.png")]
		public static const mindmap_male1Icon:Class;
		
		[Embed(source="/mindmap/icons/male2.png")]
		public static const mindmap_male2Icon:Class;
		
		[Embed(source="/mindmap/icons/males.png")]
		public static const mindmap_malesIcon:Class;
		
		[Embed(source="/mindmap/icons/fema.png")]
		public static const mindmap_femaIcon:Class;
		
		[Embed(source="/mindmap/icons/group.png")]
		public static const mindmap_groupIcon:Class;
		
		[Embed(source="/mindmap/icons/ksmiletris.png")]
		public static const mindmap_ksmiletrisIcon:Class;
		
		[Embed(source="/mindmap/icons/smiley-neutral.png")]
		public static const mindmap_smiley_neutralIcon:Class;
		
		[Embed(source="/mindmap/icons/smiley-oh.png")]
		public static const mindmap_smiley_ohIcon:Class;
		
		[Embed(source="/mindmap/icons/smiley-angry.png")]
		public static const mindmap_smiley_angryIcon:Class;
		
		[Embed(source="/mindmap/icons/smily_bad.png")]
		public static const mindmap_smily_badIcon:Class;
		
		[Embed(source="/mindmap/icons/licq.png")]
		public static const mindmap_licqIcon:Class;
		
		[Embed(source="/mindmap/icons/penguin.png")]
		public static const mindmap_penguinIcon:Class;
		
		[Embed(source="/mindmap/icons/freemind_butterfly.png")]
		public static const mindmap_freemind_butterflyIcon:Class;
		
		[Embed(source="/mindmap/icons/bee.png")]
		public static const mindmap_beeIcon:Class;
		
		[Embed(source="/mindmap/icons/forward.png")]
		public static const mindmap_forwardIcon:Class;
		
		[Embed(source="/mindmap/icons/back.png")]
		public static const mindmap_backIcon:Class;
		
		[Embed(source="/mindmap/icons/up.png")]
		public static const mindmap_upIcon:Class;
		
		[Embed(source="/mindmap/icons/down.png")]
		public static const mindmap_downIcon:Class;
		
		[Embed(source="/mindmap/icons/addition.png")]
		public static const mindmap_additionIcon:Class;
		
		[Embed(source="/mindmap/icons/subtraction.png")]
		public static const mindmap_subtractionIcon:Class;
		
		[Embed(source="/mindmap/icons/multiplication.png")]
		public static const mindmap_multiplicationIcon:Class;
		
		[Embed(source="/mindmap/icons/division.png")]
		public static const mindmap_divisionIcon:Class;
		
		[Embed(source="/mindmap/knotes.png")]
		public static const mindmap_knotesIcon:Class;
		
		[Embed(source="/mindmap/note_black_and_transp.png")]
		public static const mindmap_knotesBlackIcon:Class;
		
		[Embed(source="/mindmap/EditDetailsInDialogAction.png")]
		public static const editDetailsInDialogActionIcon:Class;
		
		[Embed(source="/mindmap/arrowDown.png")]
		public static const arrowDownIcon:Class;
		
		[Embed(source="/mindmap/arrowUp.png")]
		public static const arrowUpIcon:Class;
		
		public static const mindmapIcons:Object = {
			"help" : mindmap_helpIcon,
			"yes" : mindmap_yesIcon,
			"button_ok" : mindmap_button_okIcon,
			"button_cancel" : mindmap_button_cancelIcon,
			"bookmark" : mindmap_bookmarkIcon,
			"idea" : mindmap_ideaIcon,
			"messagebox_warning" : mindmap_messagebox_warningIcon,
			"stop-sign" : mindmap_stop_signIcon,
			"closed" : mindmap_closedIcon,
			"info" : mindmap_infoIcon,
			"clanbomber" : mindmap_clanbomberIcon,
			"checked" : mindmap_checkedIcon,
			"unchecked" : mindmap_uncheckedIcon,
			"wizard" : mindmap_wizardIcon,
			"gohome" : mindmap_gohomeIcon,
			"knotify" : mindmap_knotifyIcon,
			"password" : mindmap_passwordIcon,
			"pencil" : mindmap_pencilIcon,
			"xmag" : mindmap_xmagIcon,
			"bell" : mindmap_bellIcon,
			"bookmark" : mindmap_bookmarkIcon,
			"launch" : mindmap_launchIcon,
			"broken-line" : mindmap_broken_lineIcon,
			"stop" : mindmap_stopIcon,
			"prepare" : mindmap_prepareIcon,
			"go" : mindmap_goIcon,
			"very_negative" : mindmap_very_negativeIcon,
			"negative" : mindmap_negativeIcon,
			"neutral" : mindmap_neutralIcon,
			"positive" : mindmap_positiveIcon,
			"very_positive" : mindmap_very_positiveIcon,
			"full-1" : mindmap_full_1Icon,
			"full-2" : mindmap_full_2Icon,
			"full-3" : mindmap_full_3Icon,
			"full-4" : mindmap_full_4Icon,
			"full-5" : mindmap_full_5Icon,
			"full-6" : mindmap_full_6Icon,
			"full-7" : mindmap_full_7Icon,
			"full-8" : mindmap_full_8Icon,
			"full-9" : mindmap_full_9Icon,
			"full-0" : mindmap_full_0Icon,
			"0%" : mindmap__0PercentIcon,
			"25%" : mindmap__25PercentIcon,
			"50%" : mindmap__50PercentIcon,
			"75%" : mindmap__75PercentIcon,
			"100%" : mindmap__100PercentIcon,
			"attach" : mindmap_attachIcon,
			"desktop_new" : mindmap_desktop_newIcon,
			"list" : mindmap_listIcon,
			"edit" : mindmap_editIcon,
			"kaddressbook" : mindmap_kaddressbookIcon,
			"pencil" : mindmap_pencilIcon,
			"folder" : mindmap_folderIcon,
			"kmail" : mindmap_kmailIcon,
			"Mail" : mindmap_MailIcon,
			"revision" : mindmap_revisionIcon,
			"video" : mindmap_videoIcon,
			"audio" : mindmap_audioIcon,
			"executable" : mindmap_executableIcon,
			"image" : mindmap_imageIcon,
			"internet" : mindmap_internetIcon,
			"internet_warning" : mindmap_internet_warningIcon,
			"mindmap" : mindmap_mindmapIcon,
			"narrative" : mindmap_narrativeIcon,
			"flag-black" : mindmap_flag_blackIcon,
			"flag-blue" : mindmap_flag_blueIcon,
			"flag-green" : mindmap_flag_greenIcon,
			"flag-orange" : mindmap_flag_orangeIcon,
			"flag-pink" : mindmap_flag_pinkIcon,
			"flag" : mindmap_flagIcon,
			"flag-yellow" : mindmap_flag_yellowIcon,
			"clock" : mindmap_clockIcon,
			"clock2" : mindmap_clock2Icon,
			"hourglass" : mindmap_hourglassIcon,
			"calendar" : mindmap_calendarIcon,
			"family" : mindmap_familyIcon,
			"female1" : mindmap_female1Icon,
			"female2" : mindmap_female2Icon,
			"females" : mindmap_femalesIcon,
			"male1" : mindmap_male1Icon,
			"male2" : mindmap_male2Icon,
			"males" : mindmap_malesIcon,
			"fema" : mindmap_femaIcon,
			"group" : mindmap_groupIcon,
			"ksmiletris" : mindmap_ksmiletrisIcon,
			"smiley-neutral" : mindmap_smiley_neutralIcon,
			"smiley-oh" : mindmap_smiley_ohIcon,
			"smiley-angry" : mindmap_smiley_angryIcon,
			"smily_bad" : mindmap_smily_badIcon,
			"licq" : mindmap_licqIcon,
			"penguin" : mindmap_penguinIcon,
			"freemind_butterfly" : mindmap_freemind_butterflyIcon,
			"bee" : mindmap_beeIcon,
			"forward" : mindmap_forwardIcon,
			"back" : mindmap_backIcon,
			"up" : mindmap_upIcon,
			"down" : mindmap_downIcon,
			"addition" : mindmap_additionIcon,
			"subtraction" : mindmap_subtractionIcon,
			"multiplication" : mindmap_multiplicationIcon,
			"division" : mindmap_divisionIcon
		};
		
		[Embed(source="/properties/preference.png")]
		public static const preferenceIcon:Class;
		
		[Embed(source="/properties/preferences.png")]
		public static const preferencesIcon:Class;
		
		// CodeSync Regex
		
		[Embed(source="/codesync.regex/brick.png")]
		public static const brickIcon:Class;
		
		[Embed(source="/codesync.regex/bricks.png")]
		public static const bricksIcon:Class;
		
		[Embed(source="/codesync.regex/bullet_star.png")]
		public static const bulletIcon:Class;
		
		[Embed(source="/codesync.regex/percent.png")]
		public static const percentIcon:Class;
		
		[Embed(source="/codesync.regex/wrench.png")]
		public static const wrenchIcon:Class;
		
		// Team Git
		
		[Embed(source="/team.git/new_branch_obj.gif")]
		public static const createBranchIcon:Class;

		[Embed(source="/team.git/editconfig.gif")]
		public static const renameBranch:Class;
		
		[Embed(source="/team.git/remote_entry_tbl.gif")]
		public static const remoteEntry:Class;

		[Embed(source="/team.git/config.png")]
		public static const configBranchIcon:Class;

		[Embed(source="/team.git/remotespec.gif")]
		public static const configureFetchPush:Class;
		
		[Embed(source="/team.git/merge.gif")]
		public static const mergeBranch:Class;
		

		[Embed(source="/team.git/gitDiffFromCommits.png")]
		public static const gitDiffFromCommitsIcon:Class;
		
		[Embed(source="/team.git/gitDiffFromWorkspaceAndPatch.png")]
		public static const gitDiffFromWorkspaceAndPatch:Class;
		
		[Embed(source="/team.git/branch_obj.gif")]
		public static const branchIcon:Class;
		
		[Embed(source="/team.git/branches_obj.gif")]
		public static const branchesIcon:Class;
		
		[Embed(source="/team.git/cloneGit.gif")]
		public static const cloneRepoIcon:Class;
		
		[Embed(source="/team.git/gitrepository.gif")]
		public static const gitRepoIcon:Class;

		[Embed(source="/team.git/delete_obj.gif")]
		public static const deleteRemote:Class;
	}
}
