 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 47
!! - Block Comment, 48
# - Format Line, 25
% - Middle of Line Comment, 48
? {<exp> {,|;}} ..., 83
ABS(<nexp>), 51
ACOS(<nexp>), 55
Array.average <Average_nvar>, Array[{<start>,<length>}], 37
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 37
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 37
Array.delete Array[], Array$[] ..., 37
Array.length n, Array$[{<start>,<length>}], 37
Array.length n, Array[{<start>,<length>}], 37
Array.load Array$[], <sexp>, ..., 37
Array.load Array[], <nexp>, ..., 37
Array.max <max_nvar> Array[{<start>,<length>}], 38
Array.min <min_nvar>, Array[{<start>,<length>}], 38
Array.reverse Array$[{<start>,<length>}], 38
Array.reverse Array[{<start>,<length>}], 38
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 38
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 38
Array.shuffle Array[{<start>,<length>}], 38
Array.sort Array[{<start>,<length>}], 38
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 38
Array.sum <sum_nvar>, Array[{<start>,<length>}], 39
Array.variance <v_nvar>, Array[{<start>,<length>}], 39
ASCII(<sexp>{, <index_nexp>}), 56
ASIN(<nexp>), 55
ATAN(<nexp>), 55
ATAN2 (<nexp_y>, <nexp_x>), 55
Audio.isdone <lvar>, 154
Audio.length <length_nvar>, <aft_nexp>, 154
Audio.load <aft_nvar>, <filename_sexp>, 153
Audio.loop, 153
Audio.pause, 153
Audio.play <aft_nexp>, 153
Audio.position.current <nvar>, 154
Audio.position.seek <nexp>, 154
Audio.record.start <fn_svar>, 154
Audio.record.stop, 155
Audio.release <aft_nexp>, 154
Audio.stop, 153
Audio.volume <left_nexp>, <right_nexp>, 153
Back.resume, 77
BACKGROUND(), 57
Background.resume, 124
BAND(<nexp1>, <nexp2>), 51
BIN$(<nexp>), 60
BIN(<sexp>), 55
BNOT(<nexp>, 51
BOR(<nexp1>, <nexp2>), 51
Browse <url_sexp>, 101
Bt.close, 108
Bt.connect {0|1}, 108
Bt.device.name <svar>, 110
Bt.disconnect, 108
Bt.onReadReady.resume, 110
Bt.open {0|1}, 108
Bt.read.bytes <svar>, 110
Bt.read.ready <nvar>, 109
Bt.reconnect, 108
Bt.set.UUID <sexp>, 110
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 109
Bt.write {<exp> {,|;}} ..., 109
Bundle.clear <pointer_nexp>, 45
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 45
Bundle.create <pointer_nvar>, 44
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 44
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 44
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 44
Bundle.remove <pointer_nexp>, <key_sexp>, 45
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 45
BXOR(<nexp1>, <nexp2>), 51
Byte.close <file_table_nexp>, 96
Byte.copy <file_table_nexp>,<output_file_sexp>, 97
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 95
Byte.position.get <file_table_nexp>, <position_nexp>, 97
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 97
Byte.position.set <file_table_nexp>, <position_nexp>, 97
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 96
Byte.read.byte <file_table_nexp>, <byte_nvar>, 96
Byte.truncate <file_table_nexp>,<length_nexp>, 97
Byte.write.buffer <file_table_nexp>, <sexp>, 97
Byte.write.byte <file_table_nexp>, <byte_nexp>|<sexp>, 96
Call <user_defined_function>, 68
CBRT(<nexp>), 52
CEIL(<nexp>), 52
CHR$ (<nexp>, ...), 58
Clipboard.get <svar>, 110
Clipboard.put <sexp>, 110
CLOCK(), 57
Cls, 83
Console.line.count <count_nvar >, 84
Console.line.text <line_nexp>, <text_svar>, 84
Console.line.touched <line_nvar> {, <press_lvar>}, 84
Console.save <filename_sexp>, 84
Console.title { <title_sexp>}, 83
ConsoleTouch.resume, 77
COS(<nexp>), 54
COSH(<nexp>), 54
D_U.break, 72
D_U.continue, 72
Debug.dump.array Array[], 79
Debug.dump.bundle <bundlePtr_nexp>, 79
Debug.dump.list <listPtr_nexp>, 79
Debug.dump.scalars, 79
Debug.dump.stack <stackPtr_nexp>, 79
Debug.echo.off, 79
Debug.echo.on, 79
Debug.off, 79
Debug.on, 79
Debug.print, 79
Debug.show, 81
Debug.show.array Array[], 80
Debug.show.bundle <bundlePtr_nexp>, 80
Debug.show.list <listPtr_nexp>, 80
Debug.show.program, 81
Debug.show.scalars, 79
Debug.show.stack <stackPtr_nexp>, 81
Debug.show.watch, 81
Debug.watch var, ..., 81
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 111
Device <nvar>|<nexp>, 115
Device <svar>, 115
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 85
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 85
Dim Array [n, n, ...], Array$[n, n, ...] ..., 36
Do / Until <lexp>, 71
Echo.off, 111
Echo.on, 111
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 123
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 111
End{ <msg_sexp>}, 77
ENDS_WITH(<sub_sexp>, <base_sexp>), 57
Exit, 78
EXP(<nexp>), 54
F_N.break, 71
F_N.continue, 71
File.delete <lvar>, <path_sexp>, 91
File.dir <path_sexp>, Array[] {, <dirmark_sexp>}, 91
File.exists <lvar>, <path_sexp>, 91
File.mkdir <path_sexp>, 92
File.rename <old_path_sexp>, <new_path_sexp>, 92
File.root <svar>, 92
File.size <size_nvar>, <path_sexp>, 92
File.type <type_svar>, <path_sexp>, 92
FLOOR(<nexp>), 52
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 67
Fn.end, 68
Fn.rtn <sexp>|<nexp>, 68
Font.clear, 82
Font.delete {<font_ptr_nexp>}, 82
Font.load <font_ptr_nvar>, <filename_sexp>, 82
For - To - Step / Next, 70
FORMAT$(<pattern_sexp>, <nexp>), 65
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 65
FRAC(<nexp>), 53
Ftp.cd <new_directory_sexp>, 106
Ftp.close, 106
Ftp.delete <filename_sexp>, 107
Ftp.dir <list_nvar>, 106
Ftp.get <source_sexp>, <destination_sexp>, 106
Ftp.mkdir <directory_sexp>, 107
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 105
Ftp.put <source_sexp>, <destination_sexp>, 106
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 107
Ftp.rmdir <directory_sexp>, 107
GETERROR$(), 58
GoSub <index_nexp>, <label>... / Return, 73
GoSub <label> / Return, 73
GoTo <index_nexp>, <label>..., 73
GoTo <label>, 73
Gps.accuracy <nvar>, 161
Gps.altitude <nvar>, 161
Gps.bearing <nvar>, 161
Gps.close, 158
Gps.latitude <nvar>, 161
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 160
Gps.longitude <nvar>, 161
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 157
Gps.provider <svar>, 161
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 161
Gps.speed <nvar>, 161
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 158
Gps.time <nvar>, 161
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 135
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 143
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 144
Gr.bitmap.delete <bitmap_ptr_nexp>, 144
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 144
Gr.bitmap.drawinto.end, 145
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 145
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 143
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 144
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 143
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 143
Gr.bounded.touch touched, left, top, right, bottom, 139
Gr.bounded.touch2 touched, left, top, right, bottom, 139
Gr.brightness <nexp>, 133
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 147
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 147
Gr.camera.select 1|2, 146
Gr.camera.shoot <bm_ptr_nvar>, 146
Gr.circle <object_number_nvar>, x, y, radius, 135
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 151
Gr.close, 133
Gr.cls, 133
Gr.color {{alpha}{, red}{, green}{, blue}{, style}}, 130
Gr.front flag, 133
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 145
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 148
Gr.get.pixel x, y, alpha, red, green, blue, 148
Gr.get.position <object_ptr_nexp>, x, y, 148
Gr.get.textbounds <sexp>, left, top, right, bottom, 140
Gr.get.type <object_ptr_nexp>, <type_svar>, 148
Gr.get.value <object_ptr_nexp>, <tag_sexp>, {<value_nvar | value_svar>}, 148
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 152
Gr.group <object_number_nvar>{, <obj_nexp>}..., 137
Gr.group.getDL <object_number_nvar>, 137
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 137
Gr.group.newDL <object_number_nvar>, 137
Gr.hide <object_number_nexp>, 137
Gr.line <object_number_nvar>, x1, y1, x2, y2, 134
Gr.modify <object_ptr_nexp>, {<tag_sexp>, <value_nvar | value_svar>}, ..., 149
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 148
Gr.newDL <dl_array[{<start>,<length>}]>, 152
Gr.onGrTouch.resume, 139
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 130
Gr.orientation <nexp>, 131
Gr.oval <object_number_nvar>, left, top, right, bottom, 134
Gr.paint.get <object_ptr_nvar>, 150
Gr.point <object_number_nvar>, x, y, 134
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 136
Gr.rect <object_number_nvar>, left, top, right, bottom, 134
Gr.render, 132
Gr.rotate.end {<obj_nvar>}, 145
Gr.rotate.start angle, x, y{,<obj_nvar>}, 145
Gr.save <filename_sexp> {,<quality_nexp>}, 148
Gr.scale x_factor, y_factor, 132
Gr.screen width, height{, density }, 132
Gr.screen.to_bitmap <bm_ptr_nvar>, 147
Gr.set.antialias <lexp>, 131
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 135
Gr.set.stroke <nexp>, 131
Gr.show <object_number_nexp>, 138
Gr.show.toggle <object_number_nexp>, 138
Gr.statusbar {<height_nvar>} {, showing_lvar}, 132
Gr.statusbar.show <nexp>, 132
Gr.text.align type, 139
Gr.text.bold <lexp>, 142
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 142
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 139
Gr.text.setfont {<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>}, 141
Gr.text.size <nexp>, 139
Gr.text.skew <nexp>, 142
Gr.text.strike <lexp>, 142
Gr.text.typeface {<nexp>} {, <style_nexp>}, 141
Gr.text.underline <lexp>, 142
Gr.text.width <nvar>, <sexp>, 140
Gr.touch touched, x, y, 138
Gr.touch2 touched, x, y, 139
GR_COLLISION ( <object_1_nvar>, <object_2_nvar>), 57
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 95
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 95
Headset <state_nvar>, <type_svar>, <mic_nvar>, 123
HEX$(<nexp>), 60
HEX(<sexp>), 55
Home, 124
Html.clear.cache, 100
Html.clear.history, 101
Html.close, 100
Html.get.datalink <data_svar>, 99
Html.go.back, 100
Html.go.forward, 100
Html.load.string <html_sexp>, 99
Html.load.url <file_sexp>, 99
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 98
Html.orientation <nexp>, 98
Html.post <url_sexp>, <list_nexp>, 99
Http.post <url_sexp>, <list_nexp>, <result_svar>, 101
HYPOT(<nexp_x>, <nexp_y), 54
If / Then / Else, 70
If / Then / Else / Elseif / Endif, 69
Include FilePath, 74
Inkey$ <svar>, 86
Input {<prompt_sexp>}, <result_var>{, <default_exp>},{<canceled_nvar>}, 86
INT$(<nexp>), 60
INT(<nexp>), 53
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 56
Kb.hide, 88
Kb.toggle, 88
Key.resume, 77
LEFT$ (<sexp>, <count_nexp>), 58
LEN(<sexp>), 55
Let, 50
List.add <pointer_nexp>{, <exp>}..., 41
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 41
List.add.array string_list_pointer, Array$[{<start>,<length>}], 41
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 41
List.clear <pointer_nexp>, 42
List.create N|S, <pointer_nvar>, 41
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 42
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 42
List.remove <pointer_nexp>,<index_nexp>, 42
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 41
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 42
List.size <pointer_nexp>, <nvar>, 42
List.toArray <pointer_nexp>, Array$[] | Array[], 43
List.type <pointer_nexp>, <svar>, 42
LOG(<nexp>), 54
LOG10(<nexp>), 54
LOWER$(<sexp>), 60
MAX(<nexp>, <nexp>), 52
MenuKey.resume, 77
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 59
MIN(<nexp>, <nexp>), 52
MOD(<nexp1>, <nexp2>), 53
MyPhoneNumber <svar>, 122
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 123
OCT$(<nexp>, 60
OCT(<sexp>), 55
OnBackGround:, 124
OnBackKey:, 77
OnBtReadReady:, 110
OnConsoleTouch:, 76
OnError:, 76
OnGrTouch:, 139
OnKeyPress:, 77
OnMenuKey:, 77
OnTimer:, 115
Pause <ticks_nexp>, 117
Phone.call <sexp>, 122
Phone.dial <sexp>, 122
Phone.info <nvar>|<nexp>, 116
Phone.rcv.init, 122
Phone.rcv.next <state_nvar>, <number_svar>, 122
PI(), 54
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 87
POW(<nexp1>, <nexp2>), 54
Print {<exp> {,|;}} ..., 83
RANDOMIZE(<nexp>), 52
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 78
Read.from <nexp>, 78
Read.next <var>, ..., 78
Rem, 47
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 59
RIGHT$(<sexp>, <count_nexp>), 59
Ringer.get.mode <nvar>, 111
Ringer.get.volume <vol_nvar> { , <max_nvar>, 111
Ringer.set.mode <nexp>, 111
Ringer.set.volume <nexp>, 112
RND(), 52
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 53
Run <filename_sexp> {, <data_sexp>}, 74
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 87
Sensors.close, 163
Sensors.list <sensor_array$[]>, 162
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 163
Sensors.read sensor_type, p1, p2, p3, 163
SGN(<nexp>), 51
SHIFT (<value_nexp>, <bits_nexp>), 56
SIN(<nexp>), 54
SINH(<nexp>), 54
Sms.rcv.init, 123
Sms.rcv.next <svar>, 123
Sms.send <number_sexp>, <message_sexp>, 123
Socket.client.close, 103
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 102
Socket.client.read.file <fw_nexp>, 103
Socket.client.read.line <line_svar>, 103
Socket.client.read.ready <nvar>, 103
Socket.client.server.ip <svar>, 102
Socket.client.status <status_nvar>, 102
Socket.client.write.bytes <sexp>, 103
Socket.client.write.file <fr_nexp>, 103
Socket.client.write.line <line_sexp>, 103
Socket.myIP <svar>, 103
Socket.server.client.ip <nvar>, 105
Socket.server.close, 105
Socket.server.connect {<wait_lexp>}, 104
Socket.server.create <port_nexp>, 104
Socket.server.disconnect, 105
Socket.server.read.file <fw_nexp>, 105
Socket.server.read.line <svar>, 104
Socket.server.read.ready <nvar>, 104
Socket.server.status <status_nvar>, 104
Socket.server.write.bytes <sexp>, 105
Socket.server.write.file <fr_nexp>, 105
Socket.server.write.line <sexp>, 105
Soundpool.load <soundID_nvar>, <file_path_sexp>, 155
Soundpool.open <MaxStreams_nexp>, 155
Soundpool.pause <streamID_nexp>, 156
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 156
Soundpool.release, 156
Soundpool.resume <streamID_nexp>, 156
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 156
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 156
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 156
Soundpool.stop <streamID_nexp>, 156
Soundpool.unload <soundID_nexp>, 155
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 118
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 118
Sql.close <DB_pointer_nvar>, 125
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 127
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 126
Sql.exec <DB_pointer_nvar>, <command_sexp>, 128
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 126
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 125
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 127
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 125
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 126
Sql.query.length <length_nvar>, <cursor_nvar>, 127
Sql.query.position <position_nvar>, <cursor_nvar>, 127
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 128
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 127
SQR(<nexp>), 52
Stack.clear <ptr_nexp>, 47
Stack.create N|S, <ptr_nvar>, 46
Stack.isEmpty <ptr_nexp>, <nvar>, 47
Stack.peek <ptr_nexp>, <nvar>|<svar>, 46
Stack.pop <ptr_nexp>, <nvar>|<svar>, 46
Stack.push <ptr_nexp>, <nexp>|<sexp>, 46
Stack.type <ptr_nexp>, <svar>, 47
STARTS_WITH (<sub_sexp>, <base_sexp>{, <start_nexp>}, 56
STR$(<nexp>), 60
Stt.listen, 113
Stt.results <string_list_ptr_nvar>, 113
Su.close, 164
Su.open, 164
Su.read.line <svar>, 164
Su.read.ready <nvar>, 164
Su.write <sexp>, 164
Sw.begin <nexp>|<sexp>, 75
Sw.break, 76
Sw.case <nexp >|<sexp>, 75
Sw.default, 76
Sw.end, 76
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 118
System.close, 164
System.open, 163
System.read.line <svar>, 164
System.read.ready <nvar>, 163
System.write <sexp>, 163
TAN(<nexp>), 54
Text.close <file_table_nexp>, 93
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 88
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 93
Text.position.get <file_table_nexp>, <position_nvar>, 94
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 95
Text.position.set <file_table_nexp>, <position_nexp>, 94
Text.readln <file_table_nexp>, <line_svar>, 93
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 94
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 88
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 118
TIME(), 57
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 58
Timer.clear, 115
Timer.resume, 115
Timer.set <interval_nexp>, 115
TimeZone.get <tz_svar>, 119
TimeZone.list <tz_list_pointer_nexp>, 120
TimeZone.set { <tz_sexp> }, 119
TODEGREES(<nexp>), 55
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 120
TORADIANS(<nexp>), 55
Tts.init, 112
Tts.speak <sexp> {, <wait_lexp>}, 112
Tts.speak.toFile <sexp> {, <path_sexp>}, 112
Tts.stop, 112
UCODE(<sexp>{, <index_nexp>}), 56
UnDim Array[], Array$[], ..., 36
UPPER$(<sexp>), 60
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 60
VAL(<sexp>), 55
VERSION$(), 60
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 120
W_R.break, 71
W_R.continue, 71
WakeLock <code_nexp>, 120
While <lexp> / Repeat, 71
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 121
WifiLock <code_nexp>, 121
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 59
