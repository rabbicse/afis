USE [AMBSKE]
GO

/****** Object:  Table [dbo].[P_MemberFingerTemplate]    Script Date: 5/29/2024 4:19:00 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[P_MemberFingerTemplate](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[P_MemberId] [bigint] NOT NULL,
	[Template] [image] NOT NULL,
	[Finger] [int] NOT NULL,
	[Status] [int] NOT NULL,
	[FeatureSet] [image] NULL,
	[IsVerified] [bit] NULL,
	[VerificationStatus] [int] NULL,
	[AuthorizeStatus] [varchar](255) NULL,
 CONSTRAINT [PK_P_MemberFingerTemplate_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[P_MemberFingerTemplate] ADD  DEFAULT ((3)) FOR [VerificationStatus]
GO

ALTER TABLE [dbo].[P_MemberFingerTemplate]  WITH CHECK ADD  CONSTRAINT [FK_P_MemberFingerTemplate_P_MemberId] FOREIGN KEY([P_MemberId])
REFERENCES [dbo].[P_Member] ([Id])
GO

ALTER TABLE [dbo].[P_MemberFingerTemplate] CHECK CONSTRAINT [FK_P_MemberFingerTemplate_P_MemberId]
GO


