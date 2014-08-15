#!/usr/local/bin/perl -w
use strict;

my $home = 'C:/Users/Renan/Dropbox/PendriveOnline/SLAC/JavaWorkspace/PingERLOD/';
#The $home variable is set in the java class C.PROJECT_HOME

our (%NODE_DETAILS);
require $home.'data/perl/nodes.cf';

my $errors = "";
my $jsonStr = "{\n\t";
foreach my $key (sort keys %NODE_DETAILS) {	
			
	my @latlong = split(/ /,$NODE_DETAILS{$key}[7]);
	if ($latlong[0] eq "" || $latlong[1] eq "") {
		$errors .= "\n" . $key;
		next;
	}
	
	
	my $comments = $NODE_DETAILS{$key}[17];
	$comments =~ s/\n/\\n/g; #remove any break line in the comments. JSON library does not like break lines in strings.

	$jsonStr .= qq{
	$key: {		
			NodeName:"$key",
			NodeIP:"$NODE_DETAILS{$key}[0]",
			NodeSiteName:"$NODE_DETAILS{$key}[1]",
			NodeNickName:"$NODE_DETAILS{$key}[2]",
			NodeFullName:"$NODE_DETAILS{$key}[3]",
			LocationDescription:"$NODE_DETAILS{$key}[4]",
			country:"$NODE_DETAILS{$key}[5]",
			continent:"$NODE_DETAILS{$key}[6]",
			latitude:"$latlong[0]",
			longitude:"$latlong[1]",
			ProjectType:"$NODE_DETAILS{$key}[8]",
			PingServer:"$NODE_DETAILS{$key}[9]",
			TraceServer:"$NODE_DETAILS{$key}[10]",
			DataServer:"$NODE_DETAILS{$key}[11]",
			NodeURL:"$NODE_DETAILS{$key}[12]",
			NodeGMT:"$NODE_DETAILS{$key}[13]",
			group:"$NODE_DETAILS{$key}[14]",			
			AppUser:"$NODE_DETAILS{$key}[15]",			
			ContactInformation:"$NODE_DETAILS{$key}[16]",
			Comments:"$comments"
		},
};
}
$jsonStr =~ s{(.*),}{$1}xms; #remove the last ','
$jsonStr .= "}";
print $jsonStr;

print "\n";

print $errors;

exit 0;
