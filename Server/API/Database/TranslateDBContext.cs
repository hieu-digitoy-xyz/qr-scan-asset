using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using Asset_API.Models;

#nullable disable

namespace Asset_API.Database
{
    public partial class TranslateDBContext : DbContext
    {
        public TranslateDBContext()
        {
        }

        public TranslateDBContext(DbContextOptions<TranslateDBContext> options)
            : base(options)
        {
        }

        public virtual DbSet<DataTranslate> DataTranslates { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
//#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
                //optionsBuilder.UseSqlServer("Server=52.77.248.199;Database=TranslateCacheDB;Trusted_Connection=False;User ID=sa;Password=lenhhoXung21@@@");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasAnnotation("Relational:Collation", "SQL_Latin1_General_CP1_CI_AS");

            modelBuilder.Entity<DataTranslate>(entity =>
            {
                entity.ToTable("DataTranslate");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.DestinationLangCode)
                    .HasMaxLength(10)
                    .HasColumnName("destinationLangCode");

                entity.Property(e => e.DestinationLangName)
                    .HasMaxLength(50)
                    .HasColumnName("destinationLangName");

                entity.Property(e => e.DestinationWord).HasColumnName("destinationWord");

                entity.Property(e => e.OriginalLangCode)
                    .HasMaxLength(10)
                    .HasColumnName("originalLangCode");

                entity.Property(e => e.OriginalLangName)
                    .HasMaxLength(50)
                    .HasColumnName("originalLangName");

                entity.Property(e => e.OriginalWord).HasColumnName("originalWord");

                entity.Property(e => e.Repeat).HasColumnName("repeat");

                entity.Property(e => e.SaveDate)
                    .HasColumnType("date")
                    .HasColumnName("saveDate");

                entity.Property(e => e.Type)
                    .HasMaxLength(50)
                    .HasColumnName("type");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
