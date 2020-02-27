package com.terribleengineer.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtMaker {

	Map<String, List<?>> tmGroups;

	public JwtMaker() {
		tmGroups = new HashMap<>();
		tmGroups.put("ELASTICADMIN", new ArrayList<>());
		tmGroups.put("elastic_admins", new ArrayList<>());

		List<String> publicPermissions = new ArrayList<>();
		publicPermissions.add("WRITE");
		publicPermissions.add("EDIT");
		publicPermissions.add("DELETE");
		publicPermissions.add("READ");
		tmGroups.put("PUBLIC", publicPermissions);
	}

	public String getDevJwt() {
		return this.getJwt(JwtMaker.TEMP_SUBJECT, false, 7);
	}

	public String getJwt(String subject, Boolean isGsp, Integer tokenDurationDays) {
		JwtBuilder builder = Jwts.builder();

		// Header creation
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		Header<?> header = Jwts.header().setType("JWT");
		builder.setHeader((Map<String, Object>) header);

		// Body creation
		builder.setExpiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * tokenDurationDays));
		builder.setSubject(subject);
		builder.setIssuedAt(new Date());

		builder.claim("gsp", isGsp);
		builder.claim("service", "");
		builder.claim("type", "PKI");

		builder.claim("TM_GROUPS", tmGroups);
		builder.claim("AD_GROUPS", new ArrayList<String>());
		builder.claim("PKI_GROUPS", new HashMap<String, String>());

		return builder.signWith(key).compact();
	}

	public static void main(String[] args) {
		JwtMaker maker = new JwtMaker();
		String jwt = maker.getJwt(JwtMaker.TEMP_SUBJECT, false, 7);
		System.out.println(jwt);
	}

	public static final String TEMP_SUBJECT = "CN=Goetz Michael Ctr,OU=OU,O=Altamira,L=Default City,ST=Ohio,C=US";

}
