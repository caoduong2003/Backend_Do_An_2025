USE [master]
GO
/****** Object:  Database [UngDungHocTiengTrung]    Script Date: 03/07/2025 11:21:32 CH ******/
CREATE DATABASE [UngDungHocTiengTrung]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'UngDungHocTiengTrung', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLDUONG\MSSQL\DATA\UngDungHocTiengTrung.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'UngDungHocTiengTrung_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLDUONG\MSSQL\DATA\UngDungHocTiengTrung_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [UngDungHocTiengTrung] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [UngDungHocTiengTrung].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ARITHABORT OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET  ENABLE_BROKER 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET  MULTI_USER 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [UngDungHocTiengTrung] SET DB_CHAINING OFF 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [UngDungHocTiengTrung] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [UngDungHocTiengTrung] SET QUERY_STORE = OFF
GO
USE [UngDungHocTiengTrung]
GO
/****** Object:  Table [dbo].[BaiGiang]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BaiGiang](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[MaBaiGiang] [nvarchar](50) NOT NULL,
	[TieuDe] [nvarchar](200) NOT NULL,
	[MoTa] [nvarchar](500) NULL,
	[NoiDung] [nvarchar](max) NULL,
	[NgayTao] [datetime] NOT NULL,
	[NgayCapNhat] [datetime] NULL,
	[GiangVienID] [bigint] NOT NULL,
	[LoaiBaiGiangID] [int] NOT NULL,
	[CapDoHSK_ID] [int] NOT NULL,
	[ChuDeID] [int] NOT NULL,
	[LuotXem] [int] NULL,
	[ThoiLuong] [int] NULL,
	[HinhAnh] [nvarchar](500) NULL,
	[VideoURL] [nvarchar](500) NULL,
	[AudioURL] [nvarchar](500) NULL,
	[TrangThai] [bit] NOT NULL,
	[LaBaiGiangGoi] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CapDoHSK]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CapDoHSK](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CapDo] [int] NOT NULL,
	[TenCapDo] [nvarchar](100) NOT NULL,
	[MoTa] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChuDe]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChuDe](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TenChuDe] [nvarchar](100) NOT NULL,
	[MoTa] [nvarchar](500) NULL,
	[HinhAnh] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiBaiGiang]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiBaiGiang](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TenLoai] [nvarchar](100) NOT NULL,
	[MoTa] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MauCau]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MauCau](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[BaiGiangID] [bigint] NOT NULL,
	[TiengTrung] [nvarchar](500) NOT NULL,
	[PhienAm] [nvarchar](500) NULL,
	[TiengViet] [nvarchar](500) NOT NULL,
	[AudioURL] [nvarchar](500) NULL,
	[GhiChu] [nvarchar](1000) NULL,
	[NgayTao] [datetime] NOT NULL,
	[NgayCapNhat] [datetime] NULL,
	[TrangThai] [bit] NOT NULL,
	[ThuTu] [int] NULL,
	[DoKho] [int] NULL,
	[CapDoHSK_ID] [int] NULL,
 CONSTRAINT [PK_MauCau] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NguoiDung]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NguoiDung](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TenDangNhap] [nvarchar](50) NOT NULL,
	[Email] [nvarchar](100) NULL,
	[MatKhau] [nvarchar](255) NOT NULL,
	[HoTen] [nvarchar](100) NULL,
	[SoDienThoai] [nvarchar](20) NULL,
	[VaiTro] [int] NOT NULL,
	[TrinhDoHSK] [int] NULL,
	[HinhDaiDien] [nvarchar](500) NULL,
	[NgayTao] [datetime] NOT NULL,
	[NgayCapNhat] [datetime] NULL,
	[LanDangNhapCuoi] [datetime] NULL,
	[TrangThai] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TienTrinh]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TienTrinh](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[HocVienID] [bigint] NOT NULL,
	[BaiGiangID] [bigint] NOT NULL,
	[TrangThai] [int] NOT NULL,
	[TienDo] [int] NULL,
	[ThoiGianHoc] [int] NULL,
	[DiemSo] [int] NULL,
	[LanHoc] [int] NULL,
	[NgayBatDau] [datetime2](7) NULL,
	[NgayHoanThanh] [datetime2](7) NULL,
	[NgayCapNhat] [datetime2](7) NULL,
	[GhiChu] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TuVung]    Script Date: 03/07/2025 11:21:33 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TuVung](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[BaiGiangID] [bigint] NOT NULL,
	[TiengTrung] [nvarchar](100) NOT NULL,
	[PhienAm] [nvarchar](100) NULL,
	[TiengViet] [nvarchar](200) NOT NULL,
	[LoaiTu] [nvarchar](50) NULL,
	[ViDu] [nvarchar](max) NULL,
	[HinhAnh] [nvarchar](500) NULL,
	[AudioURL] [nvarchar](500) NULL,
	[GhiChu] [nvarchar](500) NULL,
	[CapDoHSK_ID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[BaiGiang] ON 

INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (1, N'BG001', N'Chào hỏi cơ bản', N'Bài học về cách chào hỏi cơ bản', N'Nội dung bài học chào hỏi...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), CAST(N'2025-07-03T23:17:08.573' AS DateTime), 16, 3, 1, 1, 35, 30, N'/api/media/image/book1.png', N'/api/media/video/1.Lesson_1.mp4', N'/uploads/videos/1.Lesson_1.mp4', 1, 0)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (2, N'BG002', N'Từ vựng gia đình', N'Bài học về từ vựng gia đình', N'Nội dung bài học từ vựng gia đình...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), CAST(N'2025-07-03T22:36:56.823' AS DateTime), 16, 2, 1, 2, 3, 45, N'/api/media/image/book2.png', N'/api/media/video/2.Lesson_2.mp4', N'/uploads/videos/2.Lesson_2.mp4', 1, 0)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (3, N'BG003', N'Ngữ pháp cơ bản', N'Bài học về ngữ pháp cơ bản', N'Nội dung bài học ngữ pháp...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), CAST(N'2025-06-25T14:25:21.593' AS DateTime), 17, 1, 2, 1, 1, 60, N'/api/media/image/book3.png', N'/api/media/video/3.Lesson_3.mp4', N'/uploads/videos/3.Lesson_3.mp4', 1, 1)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (4, N'BG004', N'Giới thiệu bản thân', N'Bài học về cách giới thiệu bản thân', N'Nội dung bài học giới thiệu bản thân...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), NULL, 15, 3, 1, 1, 5, 35, N'/api/media/image/book4.png', N'/api/media/video/4.Lesson_4.mp4', N'/uploads/videos/4.Lesson_4.mp4', 1, 0)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (5, N'BG005', N'Từ vựng công việc', N'Bài học về từ vựng công việc', N'Nội dung bài học từ vựng công việc...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), NULL, 15, 2, 2, 3, 12, 50, N'/api/media/image/book5.png', N'/api/media/video/5.Lesson_5.mp4', N'/uploads/videos/5.Lesson_5.mp4', 1, 1)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (6, N'BG006', N'Giao tiếp du lịch', N'Bài học về giao tiếp khi du lịch', N'Nội dung bài học giao tiếp du lịch...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), NULL, 16, 3, 2, 4, 18, 40, N'/api/media/image/book6.png', N'/api/media/video/6.Lesson_6.mp4', N'/uploads/videos/6.Lesson_6.mp4', 1, 0)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (7, N'BG007', N'Ẩm thực Trung Quốc', N'Bài học về ẩm thực Trung Quốc', N'Nội dung bài học ẩm thực...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), CAST(N'2025-06-22T06:54:29.393' AS DateTime), 17, 2, 3, 5, 26, 55, N'/api/media/image/book7.png', N'/api/media/video/7.Lesson_7.mp4', N'/uploads/videos/7.Lesson_7.mp4', 1, 1)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (8, N'BG008', N'Mua sắm và thanh toán', N'Bài học về mua sắm và thanh toán', N'Nội dung bài học mua sắm...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), NULL, 15, 3, 2, 6, 32, 42, N'/api/media/image/book8.png', N'/api/media/video/8.Lesson_8.mp4', N'/uploads/videos/8.Lesson_8.mp4', 1, 0)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (9, N'BG009', N'Thời tiết và khí hậu', N'Bài học về thời tiết và khí hậu', N'Nội dung bài học thời tiết...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), NULL, 16, 2, 3, 7, 15, 38, N'/api/media/image/book9.png', N'/api/media/video/9.Lesson_9.mp4', N'/uploads/videos/9.Lesson_9.mp4', 1, 1)
INSERT [dbo].[BaiGiang] ([ID], [MaBaiGiang], [TieuDe], [MoTa], [NoiDung], [NgayTao], [NgayCapNhat], [GiangVienID], [LoaiBaiGiangID], [CapDoHSK_ID], [ChuDeID], [LuotXem], [ThoiLuong], [HinhAnh], [VideoURL], [AudioURL], [TrangThai], [LaBaiGiangGoi]) VALUES (10, N'BG010', N'Giáo dục và học tập', N'Bài học về giáo dục và học tập', N'Nội dung bài học giáo dục...', CAST(N'2025-06-11T13:26:26.677' AS DateTime), CAST(N'2025-06-27T13:49:30.373' AS DateTime), 17, 2, 3, 8, 9, 48, N'/api/media/image/book10.png', N'/api/media/video/10.Lesson_10.mp4', N'/uploads/videos/10.Lesson_10.mp4', 1, 0)
SET IDENTITY_INSERT [dbo].[BaiGiang] OFF
GO
SET IDENTITY_INSERT [dbo].[CapDoHSK] ON 

INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (1, 1, N'HSK 1', N'Trình độ cơ bản nhất, có thể giao tiếp đơn giản')
INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (2, 2, N'HSK 2', N'Trình độ sơ cấp, có thể giao tiếp trong các tình huống hàng ngày')
INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (3, 3, N'HSK 3', N'Trình độ trung cấp, có thể hiểu và sử dụng tiếng Trung trong công việc')
INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (4, 4, N'HSK 4', N'Trình độ trung cấp nâng cao, giao tiếp tự nhiên về nhiều chủ đề')
INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (5, 5, N'HSK 5', N'Trình độ cao, có thể đọc hiểu tài liệu phức tạp')
INSERT [dbo].[CapDoHSK] ([ID], [CapDo], [TenCapDo], [MoTa]) VALUES (6, 6, N'HSK 6', N'Trình độ thành thạo, sử dụng tiếng Trung như người bản xứ')
SET IDENTITY_INSERT [dbo].[CapDoHSK] OFF
GO
SET IDENTITY_INSERT [dbo].[ChuDe] ON 

INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (1, N'Chào hỏi', N'Các bài học về chào hỏi trong tiếng Trung', N'/api/media/image/book1.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (2, N'Gia đình', N'Các bài học về từ vựng gia đình', N'/api/media/image/book2.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (3, N'Công việc', N'Các bài học về từ vựng và giao tiếp trong công việc', N'/api/media/image/book3.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (4, N'Du lịch', N'Các bài học về du lịch và khách sạn', N'/api/media/image/book4.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (5, N'Ẩm thực', N'Các bài học về món ăn và nhà hàng', N'/api/media/image/book5.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (6, N'Mua sắm', N'Các bài học về mua sắm và thanh toán', N'/api/media/image/book6.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (7, N'Thời tiết', N'Các bài học về thời tiết và khí hậu', N'/api/media/image/book7.png')
INSERT [dbo].[ChuDe] ([ID], [TenChuDe], [MoTa], [HinhAnh]) VALUES (8, N'Giáo dục', N'Các bài học về trường học và học tập', N'/api/media/image/book8.png')
SET IDENTITY_INSERT [dbo].[ChuDe] OFF
GO
SET IDENTITY_INSERT [dbo].[LoaiBaiGiang] ON 

INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (1, N'Ngữ pháp', N'Các bài học về ngữ pháp tiếng Trung')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (2, N'Từ vựng', N'Các bài học về từ vựng tiếng Trung')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (3, N'Giao tiếp', N'Các bài học về giao tiếp tiếng Trung')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (4, N'Luyện nghe', N'Các bài học phát triển kỹ năng nghe')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (5, N'Luyện nói', N'Các bài học luyện phát âm và giao tiếp')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (6, N'Đọc hiểu', N'Các bài học phát triển kỹ năng đọc hiểu')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (7, N'Viết', N'Các bài học luyện kỹ năng viết tiếng Trung')
INSERT [dbo].[LoaiBaiGiang] ([ID], [TenLoai], [MoTa]) VALUES (8, N'Văn hóa', N'Các bài học về văn hóa và phong tục Trung Quốc')
SET IDENTITY_INSERT [dbo].[LoaiBaiGiang] OFF
GO
SET IDENTITY_INSERT [dbo].[NguoiDung] ON 

INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (1, N'admin', N'admin@example.com', N'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Administrator', N'0123456789', 0, NULL, N'/uploads/images/book (1).png', CAST(N'2025-06-11T13:26:26.650' AS DateTime), NULL, NULL, 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (4, N'student1', N'student1@example.com', N'$2a$10$8cjz47bjbR4X5UZYzZzqUOQZx5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5', N'Học viên 1', N'0123456783', 2, 1, N'/uploads/images/book (4).png', CAST(N'2025-06-11T13:26:26.650' AS DateTime), CAST(N'2025-06-22T08:46:02.890' AS DateTime), NULL, 0)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (5, N'student2', N'student2@example.com', N'$2a$10$8cjz47bjbR4X5UZYzZzqUOQZx5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5', N'Học viên 2', N'0123456784', 2, 2, N'/uploads/images/book (5).png', CAST(N'2025-06-11T13:26:26.650' AS DateTime), CAST(N'2025-06-22T08:47:04.080' AS DateTime), NULL, 0)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (6, N'student3', N'student3@example.com', N'$2a$10$8cjz47bjbR4X5UZYzZzqUOQZx5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5', N'Học viên 3', N'0123456785', 2, 3, N'/uploads/images/book (6).png', CAST(N'2025-06-11T13:26:26.650' AS DateTime), NULL, NULL, 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (7, N'admin123', N'admin123@example.com', N'$2a$10$2kDoBTBLJgc9pW8AgJTHmeT7pvC1IethDhplCCrfCxl9kjYpYV8ge', N'Vu Cao DUong', N'0123456789', 0, 0, NULL, CAST(N'2025-06-11T18:17:40.253' AS DateTime), CAST(N'2025-06-11T18:17:40.253' AS DateTime), CAST(N'2025-07-02T15:45:09.207' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (10, N'hvduong123', N'hvduong@example.com', N'$2a$10$j1jx6BssAlOyxAl0Fl4ON.DMJ0pzX5SGrOiQfzUPZKjCj4c1TfWz.', N'Vu Cao Duong', N'0123456789', 2, 9, NULL, CAST(N'2025-06-11T18:30:51.840' AS DateTime), CAST(N'2025-06-11T18:30:51.843' AS DateTime), CAST(N'2025-07-02T20:32:45.843' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (11, N'duong123', N'duong@gmail.com', N'$2a$10$.vSx9BhxNf7ZOS44.8zpTO6dvYHs9UADdulIjFailLFgRWq9QoIGe', N'Vu Cao DUong', N'0987654321', 2, 0, NULL, CAST(N'2025-06-11T18:32:23.427' AS DateTime), CAST(N'2025-06-11T18:32:23.427' AS DateTime), CAST(N'2025-06-11T18:32:32.867' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (12, N'svduong', N'svduong123@gmail.com', N'$2a$10$VD.Ult06qRdro3LmqVDrA.m6WNTx437hu9Okosw9W40uVe2hV8iwy', N'Duong', N'0987654321', 2, 0, NULL, CAST(N'2025-06-18T21:42:22.760' AS DateTime), CAST(N'2025-06-18T21:42:22.760' AS DateTime), CAST(N'2025-06-18T21:42:38.387' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (15, N'gv_linhchi', N'linhchi@email.com', N'$2a$10$YbE8u2dxebRwh3E.SxxtsuDtLEc3kVl8le32s5dh96IhMdNZDA5cy', N'Trần Linh Chi', N'0901234567', 1, 6, N'/api/media/image/book1.png', CAST(N'2025-06-22T06:53:59.913' AS DateTime), NULL, CAST(N'2025-06-22T06:56:11.320' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (16, N'gv_minhhang', N'minhhang@email.com', N'$2a$10$YbE8u2dxebRwh3E.SxxtsuDtLEc3kVl8le32s5dh96IhMdNZDA5cy', N'Nguyễn Minh Hằng', N'0907654321', 1, 6, N'/api/media/image/book1.png', CAST(N'2025-06-22T06:53:59.913' AS DateTime), NULL, NULL, 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (17, N'gv_thanhtu', N'thanhtu@email.com', N'$2a$10$YbE8u2dxebRwh3E.SxxtsuDtLEc3kVl8le32s5dh96IhMdNZDA5cy', N'Lê Thành Tú', N'0912345678', 1, 6, N'/api/media/image/book1.png', CAST(N'2025-06-22T06:53:59.913' AS DateTime), NULL, NULL, 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (18, N'gv_duobg', N'gvduong123@gmail.com', N'$2a$10$v2iarwlm6Ac3A/KlwOEGseMVTGtN0SfCuKMxuoJlRWLi4cMCgu0XK', N'GVDuong', N'0987654321', 1, 0, NULL, CAST(N'2025-06-22T08:38:50.173' AS DateTime), CAST(N'2025-06-22T08:38:50.173' AS DateTime), NULL, 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (19, N'gvduong123', N'gvduong1232@gmail.com', N'$2a$10$NkwQJ4K8xS6qZSMwHh/mRuJ10H3T09MkjZusQKV/eRdx5f4cSTR9y', N'Duong', N'0987654321', 1, 0, NULL, CAST(N'2025-06-23T22:24:21.140' AS DateTime), CAST(N'2025-06-23T22:24:21.143' AS DateTime), CAST(N'2025-07-02T15:49:50.937' AS DateTime), 1)
INSERT [dbo].[NguoiDung] ([ID], [TenDangNhap], [Email], [MatKhau], [HoTen], [SoDienThoai], [VaiTro], [TrinhDoHSK], [HinhDaiDien], [NgayTao], [NgayCapNhat], [LanDangNhapCuoi], [TrangThai]) VALUES (20, N'dung123', N'dung123@gmail.com', N'$2a$10$gF6PafjTMtLj4tC/As8QfuiOpZPsqoW.dwHmj4x9nzSNvGrHdoI7m', N'dung', N'0987654321', 2, 0, NULL, CAST(N'2025-07-02T15:46:02.060' AS DateTime), CAST(N'2025-07-02T15:46:02.060' AS DateTime), CAST(N'2025-07-02T15:46:28.340' AS DateTime), 1)
SET IDENTITY_INSERT [dbo].[NguoiDung] OFF
GO
SET IDENTITY_INSERT [dbo].[TuVung] ON 

INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (2, 1, N'??', N'ni hao', N'Xin chào', N'Danh t?', N'??,?????', N'/images/hello.jpg', N'/audio/ni_hao.mp3', N'T? chào h?i co b?n nh?t', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (3, 1, N'??', N'zài jiàn', N'T?m bi?t', N'Danh t?', N'??,????', N'/images/goodbye.jpg', N'/audio/zai_jian.mp3', N'T? t?m bi?t thông d?ng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (4, 1, N'??', N'xiè xie', N'C?m on', N'Danh t?', N'???????', N'/images/thank_you.jpg', N'/audio/xie_xie.mp3', N'T? c?m on co b?n', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (5, 1, N'???', N'bù kè qì', N'Không có gì', N'C?m t?', N'???,????', N'/images/you_welcome.jpg', N'/audio/bu_ke_qi.mp3', N'Ðáp l?i l?i c?m on', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (6, 1, N'???', N'duì bù qi', N'Xin l?i', N'C?m t?', N'???,?????', N'/images/sorry.jpg', N'/audio/dui_bu_qi.mp3', N'T? xin l?i', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (7, 2, N'?', N'yi', N'M?t', N'S? t?', N'?????', N'/images/one.jpg', N'/audio/yi.mp3', N'S? 1 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (8, 2, N'?', N'èr', N'Hai', N'S? t?', N'?????', N'/images/two.jpg', N'/audio/er.mp3', N'S? 2 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (9, 2, N'?', N'san', N'Ba', N'S? t?', N'????', N'/images/three.jpg', N'/audio/san.mp3', N'S? 3 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (10, 2, N'?', N'sì', N'B?n', N'S? t?', N'?????', N'/images/four.jpg', N'/audio/si.mp3', N'S? 4 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (11, 2, N'?', N'wu', N'Nam', N'S? t?', N'????', N'/images/five.jpg', N'/audio/wu.mp3', N'S? 5 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (12, 2, N'?', N'liù', N'Sáu', N'S? t?', N'?????', N'/images/six.jpg', N'/audio/liu.mp3', N'S? 6 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (13, 2, N'?', N'qi', N'B?y', N'S? t?', N'????', N'/images/seven.jpg', N'/audio/qi.mp3', N'S? 7 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (14, 2, N'?', N'ba', N'Tám', N'S? t?', N'????', N'/images/eight.jpg', N'/audio/ba.mp3', N'S? 8 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (15, 2, N'?', N'jiu', N'Chín', N'S? t?', N'?????', N'/images/nine.jpg', N'/audio/jiu.mp3', N'S? 9 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (16, 2, N'?', N'shí', N'Mu?i', N'S? t?', N'????', N'/images/ten.jpg', N'/audio/shi.mp3', N'S? 10 trong ti?ng Trung', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (17, 3, N'??', N'bà ba', N'B?', N'Danh t?', N'??????', N'/images/father.jpg', N'/audio/ba_ba.mp3', N'T? g?i b?', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (18, 3, N'??', N'ma ma', N'M?', N'Danh t?', N'???????', N'/images/mother.jpg', N'/audio/ma_ma.mp3', N'T? g?i m?', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (19, 3, N'??', N'ge ge', N'Anh trai', N'Danh t?', N'??????', N'/images/older_brother.jpg', N'/audio/ge_ge.mp3', N'T? g?i anh trai', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (20, 3, N'??', N'jie jie', N'Ch? gái', N'Danh t?', N'???????', N'/images/older_sister.jpg', N'/audio/jie_jie.mp3', N'T? g?i ch? gái', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (21, 3, N'??', N'dì di', N'Em trai', N'Danh t?', N'???????', N'/images/younger_brother.jpg', N'/audio/di_di.mp3', N'T? g?i em trai', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (22, 3, N'??', N'mèi mei', N'Em gái', N'Danh t?', N'???????', N'/images/younger_sister.jpg', N'/audio/mei_mei.mp3', N'T? g?i em gái', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (23, 4, N'??', N'hóng sè', N'Màu d?', N'Danh t?', N'?????', N'/images/red.jpg', N'/audio/hong_se.mp3', N'Màu d?', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (24, 4, N'??', N'lán sè', N'Màu xanh duong', N'Danh t?', N'??????', N'/images/blue.jpg', N'/audio/lan_se.mp3', N'Màu xanh duong', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (25, 4, N'??', N'lu sè', N'Màu xanh lá', N'Danh t?', N'?????', N'/images/green.jpg', N'/audio/lu_se.mp3', N'Màu xanh lá', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (26, 4, N'??', N'huáng sè', N'Màu vàng', N'Danh t?', N'?????', N'/images/yellow.jpg', N'/audio/huang_se.mp3', N'Màu vàng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (27, 4, N'??', N'hei sè', N'Màu den', N'Danh t?', N'??????', N'/images/black.jpg', N'/audio/hei_se.mp3', N'Màu den', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (28, 4, N'??', N'bái sè', N'Màu tr?ng', N'Danh t?', N'?????', N'/images/white.jpg', N'/audio/bai_se.mp3', N'Màu tr?ng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (29, 5, N'??', N'mi fàn', N'Com', N'Danh t?', N'???????', N'/images/rice.jpg', N'/audio/mi_fan.mp3', N'Com tr?ng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (30, 5, N'??', N'miàn tiáo', N'Mì', N'Danh t?', N'??????', N'/images/noodles.jpg', N'/audio/mian_tiao.mp3', N'Mì s?i', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (31, 5, N'??', N'shu cài', N'Rau', N'Danh t?', N'?????????', N'/images/vegetables.jpg', N'/audio/shu_cai.mp3', N'Rau c?', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (32, 5, N'??', N'shui guo', N'Trái cây', N'Danh t?', N'???????', N'/images/fruits.jpg', N'/audio/shui_guo.mp3', N'Trái cây', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (33, 5, N'?', N'ròu', N'Th?t', N'Danh t?', N'???????', N'/images/meat.jpg', N'/audio/rou.mp3', N'Th?t', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (34, 5, N'?', N'yú', N'Cá', N'Danh t?', N'?????', N'/images/fish.jpg', N'/audio/yu.mp3', N'Cá', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (35, 6, N'?', N'mao', N'Mèo', N'Danh t?', N'?????', N'/images/cat.jpg', N'/audio/mao.mp3', N'Con mèo', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (36, 6, N'?', N'gou', N'Chó', N'Danh t?', N'?????', N'/images/dog.jpg', N'/audio/gou.mp3', N'Con chó', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (37, 6, N'?', N'niao', N'Chim', N'Danh t?', N'?????', N'/images/bird.jpg', N'/audio/niao.mp3', N'Con chim', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (38, 6, N'?', N'yú', N'Cá', N'Danh t?', N'??????', N'/images/fish_animal.jpg', N'/audio/yu_animal.mp3', N'Con cá', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (39, 6, N'?', N'ma', N'Ng?a', N'Danh t?', N'??????', N'/images/horse.jpg', N'/audio/ma.mp3', N'Con ng?a', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (40, 6, N'?', N'niú', N'Bò', N'Danh t?', N'?????', N'/images/cow.jpg', N'/audio/niu.mp3', N'Con bò', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (41, 7, N'??', N'jin tian', N'Hôm nay', N'Danh t?', N'???????', N'/images/today.jpg', N'/audio/jin_tian.mp3', N'Hôm nay', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (42, 7, N'??', N'zuó tian', N'Hôm qua', N'Danh t?', N'????????', N'/images/yesterday.jpg', N'/audio/zuo_tian.mp3', N'Hôm qua', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (43, 7, N'??', N'míng tian', N'Ngày mai', N'Danh t?', N'???????', N'/images/tomorrow.jpg', N'/audio/ming_tian.mp3', N'Ngày mai', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (44, 7, N'??', N'zao shang', N'Bu?i sáng', N'Danh t?', N'????????', N'/images/morning.jpg', N'/audio/zao_shang.mp3', N'Bu?i sáng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (45, 7, N'??', N'xià wu', N'Bu?i chi?u', N'Danh t?', N'???????', N'/images/afternoon.jpg', N'/audio/xia_wu.mp3', N'Bu?i chi?u', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (46, 7, N'??', N'wan shang', N'Bu?i t?i', N'Danh t?', N'???????', N'/images/evening.jpg', N'/audio/wan_shang.mp3', N'Bu?i t?i', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (47, 8, N'??', N'xué xiào', N'Tru?ng h?c', N'Danh t?', N'???????', N'/images/school.jpg', N'/audio/xue_xiao.mp3', N'Tru?ng h?c', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (48, 8, N'??', N'yi yuàn', N'B?nh vi?n', N'Danh t?', N'??????', N'/images/hospital.jpg', N'/audio/yi_yuan.mp3', N'B?nh vi?n', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (49, 8, N'??', N'shang diàn', N'C?a hàng', N'Danh t?', N'?????????', N'/images/shop.jpg', N'/audio/shang_dian.mp3', N'C?a hàng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (50, 8, N'??', N'yín háng', N'Ngân hàng', N'Danh t?', N'???????', N'/images/bank.jpg', N'/audio/yin_hang.mp3', N'Ngân hàng', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (51, 8, N'??', N'gong yuán', N'Công viên', N'Danh t?', N'????????', N'/images/park.jpg', N'/audio/gong_yuan.mp3', N'Công viên', 1)
INSERT [dbo].[TuVung] ([ID], [BaiGiangID], [TiengTrung], [PhienAm], [TiengViet], [LoaiTu], [ViDu], [HinhAnh], [AudioURL], [GhiChu], [CapDoHSK_ID]) VALUES (52, 8, N'???', N'tú shu guan', N'Thu vi?n', N'Danh t?', N'???????', N'/images/library.jpg', N'/audio/tu_shu_guan.mp3', N'Thu vi?n', 1)
SET IDENTITY_INSERT [dbo].[TuVung] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__BaiGiang__0773C41FB860E964]    Script Date: 03/07/2025 11:21:33 CH ******/
ALTER TABLE [dbo].[BaiGiang] ADD UNIQUE NONCLUSTERED 
(
	[MaBaiGiang] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_BaiGiang_MaBaiGiang]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_BaiGiang_MaBaiGiang] ON [dbo].[BaiGiang]
(
	[MaBaiGiang] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_BaiGiang_TieuDe]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_BaiGiang_TieuDe] ON [dbo].[BaiGiang]
(
	[TieuDe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_MauCau_BaiGiangID]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_MauCau_BaiGiangID] ON [dbo].[MauCau]
(
	[BaiGiangID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_MauCau_ThuTu]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_MauCau_ThuTu] ON [dbo].[MauCau]
(
	[BaiGiangID] ASC,
	[ThuTu] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_MauCau_TiengTrung]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_MauCau_TiengTrung] ON [dbo].[MauCau]
(
	[TiengTrung] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_MauCau_TiengViet]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_MauCau_TiengViet] ON [dbo].[MauCau]
(
	[TiengViet] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__NguoiDun__55F68FC0A6DB6191]    Script Date: 03/07/2025 11:21:33 CH ******/
ALTER TABLE [dbo].[NguoiDung] ADD UNIQUE NONCLUSTERED 
(
	[TenDangNhap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_NguoiDung_Email]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_NguoiDung_Email] ON [dbo].[NguoiDung]
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_NguoiDung_TenDangNhap]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_NguoiDung_TenDangNhap] ON [dbo].[NguoiDung]
(
	[TenDangNhap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ_TienTrinh_HocVien_BaiGiang]    Script Date: 03/07/2025 11:21:33 CH ******/
ALTER TABLE [dbo].[TienTrinh] ADD  CONSTRAINT [UQ_TienTrinh_HocVien_BaiGiang] UNIQUE NONCLUSTERED 
(
	[HocVienID] ASC,
	[BaiGiangID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_BaiGiangID]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_BaiGiangID] ON [dbo].[TienTrinh]
(
	[BaiGiangID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_HocVien_NgayCapNhat]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_HocVien_NgayCapNhat] ON [dbo].[TienTrinh]
(
	[HocVienID] ASC,
	[NgayCapNhat] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_HocVien_TrangThai]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_HocVien_TrangThai] ON [dbo].[TienTrinh]
(
	[HocVienID] ASC,
	[TrangThai] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_HocVienID]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_HocVienID] ON [dbo].[TienTrinh]
(
	[HocVienID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_NgayBatDau]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_NgayBatDau] ON [dbo].[TienTrinh]
(
	[NgayBatDau] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_NgayCapNhat]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_NgayCapNhat] ON [dbo].[TienTrinh]
(
	[NgayCapNhat] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_NgayHoanThanh]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_NgayHoanThanh] ON [dbo].[TienTrinh]
(
	[NgayHoanThanh] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_TienTrinh_TrangThai]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TienTrinh_TrangThai] ON [dbo].[TienTrinh]
(
	[TrangThai] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_TuVung_TiengTrung]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TuVung_TiengTrung] ON [dbo].[TuVung]
(
	[TiengTrung] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_TuVung_TiengViet]    Script Date: 03/07/2025 11:21:33 CH ******/
CREATE NONCLUSTERED INDEX [IX_TuVung_TiengViet] ON [dbo].[TuVung]
(
	[TiengViet] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BaiGiang] ADD  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[BaiGiang] ADD  DEFAULT ((0)) FOR [LuotXem]
GO
ALTER TABLE [dbo].[BaiGiang] ADD  DEFAULT ((1)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[BaiGiang] ADD  DEFAULT ((0)) FOR [LaBaiGiangGoi]
GO
ALTER TABLE [dbo].[MauCau] ADD  CONSTRAINT [DF_MauCau_NgayTao]  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[MauCau] ADD  CONSTRAINT [DF_MauCau_TrangThai]  DEFAULT ((1)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  DEFAULT ((2)) FOR [VaiTro]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  DEFAULT ((1)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT ((0)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT ((0)) FOR [TienDo]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT ((0)) FOR [ThoiGianHoc]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT ((1)) FOR [LanHoc]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT (getdate()) FOR [NgayBatDau]
GO
ALTER TABLE [dbo].[TienTrinh] ADD  DEFAULT (getdate()) FOR [NgayCapNhat]
GO
ALTER TABLE [dbo].[BaiGiang]  WITH CHECK ADD  CONSTRAINT [FK_BaiGiang_CapDoHSK] FOREIGN KEY([CapDoHSK_ID])
REFERENCES [dbo].[CapDoHSK] ([ID])
GO
ALTER TABLE [dbo].[BaiGiang] CHECK CONSTRAINT [FK_BaiGiang_CapDoHSK]
GO
ALTER TABLE [dbo].[BaiGiang]  WITH CHECK ADD  CONSTRAINT [FK_BaiGiang_ChuDe] FOREIGN KEY([ChuDeID])
REFERENCES [dbo].[ChuDe] ([ID])
GO
ALTER TABLE [dbo].[BaiGiang] CHECK CONSTRAINT [FK_BaiGiang_ChuDe]
GO
ALTER TABLE [dbo].[BaiGiang]  WITH CHECK ADD  CONSTRAINT [FK_BaiGiang_GiangVien] FOREIGN KEY([GiangVienID])
REFERENCES [dbo].[NguoiDung] ([ID])
GO
ALTER TABLE [dbo].[BaiGiang] CHECK CONSTRAINT [FK_BaiGiang_GiangVien]
GO
ALTER TABLE [dbo].[BaiGiang]  WITH CHECK ADD  CONSTRAINT [FK_BaiGiang_LoaiBaiGiang] FOREIGN KEY([LoaiBaiGiangID])
REFERENCES [dbo].[LoaiBaiGiang] ([ID])
GO
ALTER TABLE [dbo].[BaiGiang] CHECK CONSTRAINT [FK_BaiGiang_LoaiBaiGiang]
GO
ALTER TABLE [dbo].[MauCau]  WITH CHECK ADD  CONSTRAINT [FK_MauCau_BaiGiang] FOREIGN KEY([BaiGiangID])
REFERENCES [dbo].[BaiGiang] ([ID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[MauCau] CHECK CONSTRAINT [FK_MauCau_BaiGiang]
GO
ALTER TABLE [dbo].[MauCau]  WITH CHECK ADD  CONSTRAINT [FK_MauCau_CapDoHSK] FOREIGN KEY([CapDoHSK_ID])
REFERENCES [dbo].[CapDoHSK] ([ID])
GO
ALTER TABLE [dbo].[MauCau] CHECK CONSTRAINT [FK_MauCau_CapDoHSK]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [FK_TienTrinh_BaiGiang] FOREIGN KEY([BaiGiangID])
REFERENCES [dbo].[BaiGiang] ([ID])
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [FK_TienTrinh_BaiGiang]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [FK_TienTrinh_HocVien] FOREIGN KEY([HocVienID])
REFERENCES [dbo].[NguoiDung] ([ID])
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [FK_TienTrinh_HocVien]
GO
ALTER TABLE [dbo].[TuVung]  WITH CHECK ADD  CONSTRAINT [FK_TuVung_BaiGiang] FOREIGN KEY([BaiGiangID])
REFERENCES [dbo].[BaiGiang] ([ID])
GO
ALTER TABLE [dbo].[TuVung] CHECK CONSTRAINT [FK_TuVung_BaiGiang]
GO
ALTER TABLE [dbo].[TuVung]  WITH CHECK ADD  CONSTRAINT [FK_TuVung_CapDoHSK] FOREIGN KEY([CapDoHSK_ID])
REFERENCES [dbo].[CapDoHSK] ([ID])
GO
ALTER TABLE [dbo].[TuVung] CHECK CONSTRAINT [FK_TuVung_CapDoHSK]
GO
ALTER TABLE [dbo].[MauCau]  WITH CHECK ADD  CONSTRAINT [CK_MauCau_DoKho] CHECK  (([DoKho]>=(1) AND [DoKho]<=(3) OR [DoKho] IS NULL))
GO
ALTER TABLE [dbo].[MauCau] CHECK CONSTRAINT [CK_MauCau_DoKho]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [CK_NguoiDung_VaiTro] CHECK  (([VaiTro]=(2) OR [VaiTro]=(1) OR [VaiTro]=(0)))
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [CK_NguoiDung_VaiTro]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [CK_TienTrinh_DiemSo] CHECK  (([DiemSo] IS NULL OR [DiemSo]>=(0) AND [DiemSo]<=(100)))
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [CK_TienTrinh_DiemSo]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [CK_TienTrinh_LanHoc] CHECK  (([LanHoc]>=(1)))
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [CK_TienTrinh_LanHoc]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [CK_TienTrinh_ThoiGianHoc] CHECK  (([ThoiGianHoc]>=(0)))
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [CK_TienTrinh_ThoiGianHoc]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [CK_TienTrinh_TienDo] CHECK  (([TienDo]>=(0) AND [TienDo]<=(100)))
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [CK_TienTrinh_TienDo]
GO
ALTER TABLE [dbo].[TienTrinh]  WITH CHECK ADD  CONSTRAINT [CK_TienTrinh_TrangThai] CHECK  (([TrangThai]=(2) OR [TrangThai]=(1) OR [TrangThai]=(0)))
GO
ALTER TABLE [dbo].[TienTrinh] CHECK CONSTRAINT [CK_TienTrinh_TrangThai]
GO
USE [master]
GO
ALTER DATABASE [UngDungHocTiengTrung] SET  READ_WRITE 
GO
