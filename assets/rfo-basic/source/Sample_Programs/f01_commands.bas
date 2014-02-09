 There is 100+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 40
!! - Block Comment, 40
# - Format Line, 21
% - Middle of Line Comment, 41
ABS(<nexp>), 44
ACOS(<nexp>), 46
Array.average <Average_nvar>, Array[], 31
Array.copy SourceArray[{<start>{,<length>}], DestinationArray[{{-}<extras>}, 31
Array.delete Array[]{, Array[]} ..., 32
Array.length <Length_nvar>, Array[], 32
Array.load Array[], <nexp>{,<nexp>, ...,<nexp>}, 32
Array.max <Max_nvar> Array[], 32
Array.min <Min_nvar>, Array[], 32
Array.reverse Array[] | Array$[], 32
Array.search Array[]|Array$[], <value_nexp>|<value$_sexp>, <result_nvar>{,<start_nexp>}, 32
Array.shuffle Array[] | Array$[], 33
Array.std_dev <sd_nvar>, Array[], 33
Array.sum <sum_nvar>, Array[], 33
Array.variance <v_nvar>, Array[], 33
ASCII(<sexp>), 47
ASIN(<nexp>), 46
ATAN(<nexp>), 46
ATAN2 (<nexp_x>, <nexp_y>), 46
Audio.isdone <lvar>, 121
Audio.length <length_nvar>, <aft_nexp>, 121
Audio.load  <aft_nvar>, <filename_sexp>, 120
Audio.loop, 121
Audio.pause, 121
Audio.play <aft_nexp>, 120
Audio.position.current  <nvar>, 121
Audio.position.seek <nexp>, 121
Audio.record.start <fn_svar>, 122
Audio.record.stop, 122
Audio.release <aft_nexp>, 121
Audio.stop, 120
Audio.volume <left_nexp>, <right_nexp>, 121
Back.resume, 59
Background(), 48
Background.Resume, 97
BAND(<nexp1>, <nexp2>), 43
BIN$(<nexp>), 50
BIN(<sexp>), 46
BOR(<nexp1>, <nexp2>), 43
Browse <url_sexp>, 86
Bt.close, 84
Bt.connect {0|1}, 84
Bt.device.name <svar>, 85
Bt.disconnect, 84
Bt.onReadReady.Resume, 85
Bt.open {0|1}, 83
Bt.read.bytes <svar>, 85
Bt.read.ready <nvar>, 85
Bt.reconnect, 84
Bt.set.uuid <sexp>, 85
Bt.status <nvar>, 84
Bt.write <sexp>|<nexp> {,|;} . . . . <sexp>|<nexp>{,|;}, 84
Bundle.clear <pointer_nexp>, 38
Bundle.contain  <pointer_nexp>, <key_sexp> , <contains_nvar>, 38
Bundle.create <pointer_nvar>, 37
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 38
Bundle.keys <pointer_nexp>, <list_nvar>, 38
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 37
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 38
BXOR(<nexp1>, <nexp2>), 44
Byte.close <file_table_nvar>, 73
Byte.copy <file_table_nvar>,<output_file_svar>, 74
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 73
Byte.position.get  <file_table_nvar>, <position_nexp>, 74
Byte.position.set <file_table_nvar>, <position_nexp>, 74
Byte.read.buffer <file_table_nvar>, <count_nexp>, <buffer_svar>, 74
Byte.read.byte <file_table_nvar>, <byte_nvar>, 73
Byte.write.buffer <file_table_nvar>, <sexp>, 74
Byte.write.byte <file_table_nvar>, <byte_nexp>|<sexp>, 74
Call <user_defined_function>, 53
CBRT(<nexp>), 44
CEIL(<nexp>), 44
CHR$ (<nexp>), 49
Clipboard.get <svar>, 86
Clipboard.put <sexp>, 86
Clock(), 48
CLS, 65
Console.line.text <line_nexp>, <text_svar>, 65
Console.line.touched <line_nvar> {, <press_lvar>}, 65
Console.save <filename_sexp>, 66
Console.Title { <title_sexp>}, 64
ConsoleTouch.Resume, 59
COS(<nexp>), 45
COSH(<nexp>), 45
D_U.break, 56
D_U.continue, 56
Debug.dump.array Array[], 62
Debug.dump.bundle  <bundlePtr_nexp>, 62
Debug.dump.list  <listPtr_nexp>, 62
Debug.dump.scalars, 62
Debug.dump.stack  <stackPtr_nexp>, 62
Debug.echo.off, 61
Debug.echo.on, 61
Debug.off, 61
Debug.on, 61
Debug.print, 62
Debug.show, 64
Debug.show.array Array[], 62
Debug.show.bundle  <bundlePtr_nexp>, 63
Debug.show.list  <listPtr_nexp>, 63
Debug.show.program, 64
Debug.show.scalars, 62
Debug.show.stack  <stackPtr_nexp>, 63
Debug.show.watch, 63
Debug.watch var, var, …, var, 63
Decrypt  <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 87
Device <svar>, 90
Dim Array[<nexp>{, <nexp> } ... ] {, Array[<nexp>{, <nexp> } ... ] } ..., 31
Do - Until <lexp>, 55
Echo.off, 86
Echo.on, 86
Email.send <recipient_sxep>, <subject_sexp>, <body_sexp>, 96
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 87
End{ <msg_sexp>}, 60
Ends_with ( <Look_for_sexp>, <look_in_sexp>), 47
Exit, 60
EXP(<nexp>), 45
F_N.break, 55
F_N.continue, 55
File.delete <lvar>, <path_sexp>, 69
File.dir <path_sexp>, Array[], 69
File.exists <lvar>, <path_sexp>, 70
File.mkdir <path_sexp>, 70
File.rename <Old_Path_sexp>, <New_Path_sexp>, 70
File.root <svar>, 70
File.size <size_nvar>, <path_sexp>, 70
FLOOR(<nexp>), 45
Fn.def  name|name$( {nvar}|{svar}|Array[]|Array$[], ...{nvar}|{svar}|Array[]|Array$[]), 51
Fn.end, 53
Fn.rtn <sexp>|<nexp>, 53
For - To - Step - Next, 54
FORMAT$(<pattern_sexp>, <nexp>), 50
Ftp.cd <new_directory_sexp>, 82
Ftp.close, 81
Ftp.delete <filename_sexp>, 82
Ftp.dir <list_nvar>, 82
Ftp.get <source_sexp>, <destination_sexp>, 82
Ftp.mkdir <directory_sexp>, 82
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 81
Ftp.put <source_sexp>, <destination_sexp>, 81
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 82
Ftp.rmdir <directory_sexp>, 82
GETERROR$(), 49
GoSub <label>, Return, 56
GoTo <label>, 57
Gps.accuracy <nvar>, 124
Gps.altitude <nvar>, 125
Gps.bearing <nvar>, 125
Gps.close, 124
Gps.latitude <nvar>, 124
Gps.longitude <nvar>, 124
Gps.open, 124
Gps.provider <svar>, 124
Gps.speed <nvar>, 125
Gps.time <nvar>, 125
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 106
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 111
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 112
Gr.bitmap.delete <bitmap_ptr_nvar>, 112
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 112
Gr.bitmap.drawinto.end, 113
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 113
gr.bitmap.load bitmap_ptr, File_name$, 111
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 112
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lvar> }, 111
Gr.bitmap.size <bitmap_ptr_nvar>, width, height, 111
Gr.bounded.touch touched, left, top, right, bottom, 109
Gr.bounded.touch2 touched, left, top, right, bottom, 109
Gr.brightness <nexp>, 105
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 115
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 115
Gr.camera.select 1|2, 114
Gr.camera.shoot <bm_ptr_nvar>, 114
Gr.circle <object_number_nvar>, x, y, radius, 106
Gr.clip <object__ptr_nvar>,  <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 118
Gr.close, 104
Gr.cls, 104
Gr.color alpha, red, green, blue, <style_nexp>, 102
Gr.front  flag, 104
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 112
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 116
Gr.get.pixel x, y, alpha, red, green, blue, 115
Gr.get.position <object_ptr_nvar>,  x, y, 116
Gr.get.textbounds <sexp>, left, top, right, bottom, 110
Gr.get.type <object_ptr_nvar>, <type_svar>, 116
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 116
Gr.GetDL <dl_array[]> {, <keep_all_objects_lexp> }, 119
Gr.hide <object_number_nvar>, 108
Gr.line <object_number_nvar>, x1, y1, x2, y2, 105
Gr.modify <object_ptr_nvar>,  <tag_sexp>, {<value_nvar | value_svar>}, 116
Gr.NewDL <dl_array[]>, 119
Gr.onGRTouch.resume, 109
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <orientation_nexp>}}, 102
Gr.orientation <nexp>, 103
Gr.oval <object_number_nvar>, left, top, right, bottom, 106
Gr.paint.get <object_ptr_nvar>, 117
Gr.point <object_number_nvar>, x, y, 105
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 107
Gr.rect <object_number_nvar>, left, top, right, bottom, 106
Gr.render, 103
Gr.rotate.end {<obj_nvar>}, 113
Gr.rotate.start angle, x, y{,<obj_nvar>}, 113
Gr.save <filename_sexp> {,<quality_nexp>}, 115
Gr.scale x_factor, y_factor, 104
Gr.screen width, height{, density }, 103
Gr.screen.to_bitmap <bm_ptr_nvar>, 115
Gr.set.AntiAlias <lexp>, 103
Gr.set.pixels <object_number_nvar>, pixels[] {,x,y}, 107
Gr.set.stroke <nexp>, 103
Gr.show <object_number_nvar>, 108
Gr.StatusBar.Show  <nexp>, 103
Gr.text.align type, 109
Gr.text.bold <lexp>, 110
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 111
Gr.text.size <nexp>, 109
Gr.text.skew <nexp>, 110
Gr.text.strike <lexp>, 110
Gr.text.typeface <nexp>, 110
Gr.text.underline <lexp>, 110
Gr.text.width <nvar>, <sexp>, 109
Gr.touch touched, x, y, 108
Gr.touch2 touched, x, y, 109
Gr_collision ( <object_1_nvar>, <object_2_nvar>), 47, 118
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 72
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 72
Headset <state_nvar>, <type_svar>, <mic_nvar>, 96
HEX$(<nexp>), 50
HEX(<sexp>), 46
Home, 97
Html.clear.cache, 77
Html.clear.history, 77
Html.close, 77
Html.get.datalink <data_svar>, 76
Html.go.back, 77
Html.go.forward, 77
Html.load.string <html_sexp>, 76
Html.load.url <file_sexp>, 75
Html.open {<show_status_bar_nexp>}, 75
Html.post <url _sexp>, <list_nexp>, 76
Http.post <url_sexp>, <list_nexp>, <result_svar>, 94
HYPOT(<nexp_x>, <nexp_y), 45
If  - Then - Else, 54
If  - Then - Else - Elseif - Endif, 53
Include FileNamePath, 90
Inkey$ <svar>, 66
Input <Prompt_sexp>, <nvar>|<svar>, {<Default_sexp>|<Default_nexp>}, 66
Is_In(<Search_for_sexp>, <Search_in_sexp>{, <start_nexp>}, 47
Kb.hide, 67
Kb.toggle, 67
Key.Resume, 60
LEFT$ (<sexp>, <nexp>), 49
LEN(<sexp>), 46
LET, 43
List.add <pointer_nexp>, <nexp>{,<nexp>..,<nexp>}, 35
List.add.array  <destination_list_pointer_nexp>,Array$[]|Array[], 35
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 35
List.clear <pointer_nexp>, 36
List.create N|S, <pointer_nvar>, 35
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 36
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 36
List.remove <pointer_nexp>,<index_nexp>, 36
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 35
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 36
List.size <pointer_nexp>, <nvar>, 36
List.ToArray <pointer_nexp>, Array$[] | Array[], 37
List.type <pointer_nexp>, <svar>, 36
LOG(<nexp>), 45
LOG10(<nexp>), 45
LOWER$(<sexp>), 50
MAX(<nexp>, <nexp>), 44
MenuKey.Resume, 60
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 49
MIN(<nexp>, <nexp>), 44
MOD(<nexp1>, <nexp2>), 45
MyPhoneNumber <svar>, 95
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 96
OCT$(<nexp>, 50
OCT(<sexp>), 46
OnBackGround:, 97
OnBackKey:, 59
OnBtReadReady:, 85
OnConsoleTouch:, 59
OnError:, 58
OnGRTouch:, 109
OnKeyPress:, 60
OnMenuKey:, 60
OnTimer:, 89
Pause <ticks_nexp>, 90
Phone.call <sexp>, 95
Phone.rcv.init, 95
Phone.rcv.next  <state_nvar>, <number_svar>, 95
PI(), 45
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 90
POW(<nexp1>, <nexp2>), 45
Print <sexp>|<nexp> {,|;} . . . . <sexp>|<nexp>{,|;}, 65
RANDOMIZE(<nexp>), 44
Read.data <number>|<string> {,<number>|<string>…,<number>|<string>}, 60
Read.from <nexp>, 61
Read.next  <svar>|<nvar>{,<svar>|<nvar>… , <svar>|<nvar>}, 61
REM, 40
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 49
RIGHT$(<sexp>, <nexp>), 49
RND(), 44
ROUND(<nexp>), 45
Run <filename_sexp> {, <data_sexp>}, 57
Select <selection_nvar>, < Array$[]>|<list_nexp>, <message_sexp> {,<press_nvar>}, 91
Sensors.close, 126
Sensors.list <sensor_array$[]>, 126
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 126
Sensors.read sensor_type, p1, p2, p3, 126
SHIFT (<value_nexp>, <bits_nexp>), 47
SIN(<nexp>), 45
SINH(<nexp>), 46
Sms.rcv.init, 95
Sms.rcv.next <svar>, 95
Sms.send <number_sexp>, <message_sexp>, 95
Socket.client.close, 79
Socket.client.connect <server_ip_sexp>, <port_nexp>, 78
Socket.client.read.file  <fw_nexp>, 79
Socket.client.read.line <line_svar>, 78
Socket.client.read.ready <nvar>, 79
Socket.client.server.ip <svar>, 78
Socket.client.status <status_nvar>, 78
Socket.client.write.bytes <sexp>, 79
Socket.client.write.file  <fr_nexp>, 79
Socket.client.write.line <line_sexp>, 79
Socket.myip  <svar>, 79
Socket.server.client.ip <nvar>, 81
Socket.server.close, 81
Socket.server.connect {<wait_lexp>}, 80
Socket.server.create <port_nexp>, 80
Socket.server.disconnect, 81
Socket.server.read.file <fw_nexp>, 81
Socket.server.read.line <svar>, 80
Socket.server.read.ready <nvar>, 80
Socket.server.status <status_nvar>, 80
Socket.server.write.bytes <sexp>, 80
Socket.server.write.file  <fr_nexp>, 81
Socket.server.write.line <sexp>, 80
Soundpool.load <soundID_nvar>, <file_path_sexp>, 123
Soundpool.open <MaxStreams_nexp>, 122
Soundpool.pause <streamID_nexp>, 124
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 123
Soundpool.release, 124
Soundpool.resume <streamID_nexp>, 124
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 123
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 123
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 123
Soundpool.stop <streamID_nexp>, 124
Soundpool.unload <soundID_nexp>, 123
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 91
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 91
Sql.close <DB_pointer_nvar>, 98
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 100
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 98
Sql.exec <DB_pointer_nvar>, <command_sexp>, 100
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$, 98
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 98
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 99
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 97
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 99
Sql.query.length <length_nvar>, <cursor_nvar>, 99
Sql.query.position <position_nvar>, <cursor_nvar>, 99
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 100
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 100
SQR(<nexp>), 44
Stack.clear <ptr_nexep>, 40
Stack.create N|S, <ptr_nvar>, 39
Stack.IsEmpty <ptr_nexep>, <nvar>, 40
Stack.peek <ptr_nexep>, <nvar>|<svar>, 39
Stack.pop <ptr_nexep>, <nvar>|<svar>, 39
Stack.push <ptr_nexep>, <nexp>|<sexp>, 39
Stack.type <ptr_nexep>, <svar>, 39
Starts_with (<Search_for_sexp>, <Search_in_sexp>{,<start_nexp>}, 47
STR$(<sexp>), 50
Stt.listen, 88
Stt.results <string_list_ptr_nvar>, 88
Su.close, 128
Su.open, 127
Su.read.line <svar>, 128
Su.read.ready <nvar>, 127
Su.write <sexp>, 127
Sw.begin <nexp>|<sexp>, 58
Sw.break, 58
Sw.case <nexp >|<sexp>, 58
Sw.default, 58
Sw.end, 58
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 86
System.close, 127
System.open, 127
System.read.line <svar>, 127
System.read.ready <nvar>, 127
System.write <sexp>, 127
TAN(<nexp>), 45
Text.close <file_table_nvar>, 71
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 67
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 71
Text.position.get  <file_table_nvar>, <position_nvar>, 72
Text.position.set <file_table_nvar>, <position_nexp>, 72
Text.readln <file_table_nvar>, <line_svar>, 71
Text.writeln <file_table_nexp>, <parms same as print>, 72
TGet <result_svar>, <prompt_sexp> {,  <title_sexp>}, 67
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 92
TIME(), 48
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 48
Timer.Clear, 90
Timer.Resume, 89
Timer.set <interval_nexp>, 89
TimeZone.get <tz_svar>, 93
TimeZone.list <tz_list_pointer_nvar>, 93
TimeZone.set { <tz_sexp> }, 93
TODEGREES(<nexp>), 46
Tone <frequency_nexp>, <duration_nexp>, 93
TORADIANS(<nexp>), 46
Tts.init, 87
Tts.speak  <sexp> {, <wait_lexp>}, 87
Tts.speak.toFile <sexp> {, <path_sexp>}, 87
Tts.stop, 87
UCODE(<sexp>), 47
UnDim Array[]{, Array[] } ..., 31
UPPER$(<sexp>), 50
VAL( <sexp> ), 46
VERSION$(), 50
Vibrate <pattern_array[]>,<nexp>, 93
W_R.break, 55
W_R.continue, 55
WakeLock <code_nexp>, 94
While <lexp> - Repeat, 55
WORD$(<source_sexp>, <n_nexp> {, <delimiter_sexp>}), 50
