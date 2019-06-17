INSERT INTO `oauth_client_details` (
    `client_id`,
    `resource_ids`,
    `client_secret`,
    `scope`,
    `authorized_grant_types`,
    `web_server_redirect_uri`,
    `authorities`,
    `access_token_validity`,
    `refresh_token_validity`,
    `additional_information`,
    `autoapprove`
)VALUES(
    'devglan-client',
    NULL,
    '$2a$10$CcbFh4vRAoOOh.kTj/BPfebSmV89dJ1J6ImLn/dhe0N9GrkBcFOfS', -- System.out.println(passwordEncoder.encode("devglan-secret"));
    'read, write, trust',
    'password, authorization_code, refresh_token, implicit',
    NULL,
    NULL,
    3600,  -- 60*60 (1 hour)
    18000, -- 6*60*60 (6 Hours)
    NULL,
    NULL
);
