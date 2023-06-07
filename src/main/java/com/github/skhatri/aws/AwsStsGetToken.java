package com.github.skhatri.aws;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.PolicyDescriptorType;


public class AwsStsGetToken {
    private static final String IAM = String.format("arn:aws:iam::%s", System.getenv("AWS_ACCOUNT_ID"));

    public static void main(String[] args) {
        String policy = """
{
  "Version": "2012-10-17",
  "Statement": [
      {
          "Effect": "Allow",
          "Action": ["s3:GetObject", "s3:GetObjectAcl", "s3:GetObjectAttributes", "s3:GetObjectTagging"],
          "Resource": "arn:aws:s3:::aws-sts-data-files/technology/**/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:GetObject", "s3:GetObjectAcl", "s3:GetObjectAttributes", "s3:GetObjectTagging"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/deals/**/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:GetObject", "s3:GetObjectAcl", "s3:GetObjectAttributes", "s3:GetObjectTagging"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/science/**/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:GetObject*"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/questions/*/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:Get*"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/sports/*/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:GetObject*", "s3:Put*"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/finance/*-05-*/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:GetObject", "s3:GetObject*"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/worldnews/**/*"
      },
      {
        "Effect": "Allow",
        "Action": ["s3:PutObject*"],
        "Resource": "arn:aws:s3:::aws-sts-data-files/technology/**/*"
    },
    {
      "Effect": "Allow",
      "Action": ["s3:Put*", "s3:GetObject*"],
      "Resource": "arn:aws:s3:::aws-sts-data-files/deals/**/*"
    },
      {
          "Effect": "Allow",
          "Action": ["s3:List*"],
          "Resource": "arn:aws:s3:::aws-sts-data-files*"
      }
  ]
}
                """;

        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest().withRoleArn(IAM + ":role/s3-full-access")
                .withRoleSessionName("random-token")
                .withDurationSeconds(43200)
                .withPolicyArns(
                        new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/service-role/ROSAKMSProviderPolicy")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/ReadOnlyAccess")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/AWSCloudFormationReadOnlyAccess")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/CloudWatchReadOnlyAccess")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/CloudWatchActionsEC2Access")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/CloudSearchReadOnlyAccess")
                        , new PolicyDescriptorType().withArn("arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess")
                )
                .withPolicy(policy);
        System.out.println("Policy raw length " + policy.length());
        AWSSecurityTokenService tokenService = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(
                        new ProfileCredentialsProvider("awssts") //in .aws/credentials
                )
                .build();
        AssumeRoleResult result = tokenService.assumeRole(assumeRoleRequest);
        System.out.println("policy size " + result.getPackedPolicySize());
        Credentials credentials = result.getCredentials();

        System.out.println("[token]\n"
                + "aws_access_key_id = " + credentials.getAccessKeyId() + "\n"
                + "aws_secret_access_key = " + credentials.getSecretAccessKey() + "\n"
                + "aws_session_token = " + credentials.getSessionToken());
        System.out.println("");
        System.out.println("export AWS_ACCESS_KEY_ID=\"" + credentials.getAccessKeyId() + "\"\n"
                + "export AWS_SECRET_ACCESS_KEY=\"" + credentials.getSecretAccessKey() + "\"\n"
                + "export AWS_SESSION_TOKEN=\"" + credentials.getSessionToken() + "\"\n"
                + "aws s3 cp s3://aws-sts-data-files/deals/date=2023-02-05/weather.parquet .");
    }
}
