using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace Asset_API.Models
{
    public partial class AssetDBContext : DbContext
    {
        public AssetDBContext()
        {
        }

        public AssetDBContext(DbContextOptions<AssetDBContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Asset> Assets { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            //if (!optionsBuilder.IsConfigured)
//            {
//#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
//                optionsBuilder.UseSqlServer("Server=52.77.248.199;Database=AssetDB;Trusted_Connection=False;User ID=sa;Password=lenhhoXung21@@@");
//            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Asset>(entity =>
            {
                entity.HasKey(e => e.PartId);

                entity.ToTable("Asset");

                entity.Property(e => e.PartId)
                    .HasMaxLength(20)
                    .HasColumnName("partId");

                entity.Property(e => e.AlpSop).HasColumnName("alpSop");

                entity.Property(e => e.Brand)
                    .HasMaxLength(150)
                    .HasColumnName("brand");

                entity.Property(e => e.Category)
                    .HasMaxLength(150)
                    .HasColumnName("category");

                entity.Property(e => e.Description).HasColumnName("description");

                entity.Property(e => e.Import).HasColumnName("import");

                entity.Property(e => e.Local).HasColumnName("local");

                entity.Property(e => e.Note).HasColumnName("note");

                entity.Property(e => e.Search).HasColumnName("search");

                entity.Property(e => e.StockAmount).HasColumnName("stockAmount");

                entity.Property(e => e.StorageBin)
                    .HasMaxLength(50)
                    .HasColumnName("storageBin");

                entity.Property(e => e.Supplier)
                    .HasMaxLength(150)
                    .HasColumnName("supplier");

                entity.Property(e => e.UseFor)
                    .HasMaxLength(150)
                    .HasColumnName("useFor");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
