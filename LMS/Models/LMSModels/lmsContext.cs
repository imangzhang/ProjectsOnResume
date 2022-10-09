using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace LMS.Models.LMSModels
{
    public partial class LMSContext : DbContext
    {
        public LMSContext()
        {
        }

        public LMSContext(DbContextOptions<LMSContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Administrator> Administrators { get; set; } = null!;
        public virtual DbSet<Assignment> Assignments { get; set; } = null!;
        public virtual DbSet<AssignmentCategory> AssignmentCategories { get; set; } = null!;
        public virtual DbSet<Class> Classes { get; set; } = null!;
        public virtual DbSet<Course> Courses { get; set; } = null!;
        public virtual DbSet<Department> Departments { get; set; } = null!;
        public virtual DbSet<EnrollIn> EnrollIns { get; set; } = null!;
        public virtual DbSet<Professor> Professors { get; set; } = null!;
        public virtual DbSet<Student> Students { get; set; } = null!;
        public virtual DbSet<Submission> Submissions { get; set; } = null!;
        public virtual DbSet<User> Users { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseMySql("name=LMS:LMSConnectionString", Microsoft.EntityFrameworkCore.ServerVersion.Parse("10.1.48-mariadb"));
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.UseCollation("latin1_swedish_ci")
                .HasCharSet("latin1");

            modelBuilder.Entity<Administrator>(entity =>
            {
                entity.HasKey(e => e.UId)
                    .HasName("PRIMARY");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.HasOne(d => d.UIdNavigation)
                    .WithOne(p => p.Administrator)
                    .HasForeignKey<Administrator>(d => d.UId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Administrators_ibfk_1");
            });

            modelBuilder.Entity<Assignment>(entity =>
            {
                entity.HasIndex(e => new { e.Acname, e.AccatalogId, e.Acsemester }, "Assignments_ref_AC");

                entity.Property(e => e.AssignmentId)
                    .HasColumnType("int(10) unsigned")
                    .ValueGeneratedNever()
                    .HasColumnName("AssignmentID");

                entity.Property(e => e.AccatalogId)
                    .HasColumnType("int(11)")
                    .HasColumnName("ACcatalogID");

                entity.Property(e => e.Acname)
                    .HasMaxLength(100)
                    .HasColumnName("ACName");

                entity.Property(e => e.Acsemester)
                    .HasMaxLength(10)
                    .HasColumnName("ACSemester");

                entity.Property(e => e.Contents).HasMaxLength(8192);

                entity.Property(e => e.DueDateTime).HasColumnType("datetime");

                entity.Property(e => e.MaxPointValue).HasColumnType("int(11)");

                entity.Property(e => e.Name).HasMaxLength(100);

                entity.HasOne(d => d.Ac)
                    .WithMany(p => p.Assignments)
                    .HasForeignKey(d => new { d.Acname, d.AccatalogId, d.Acsemester })
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Assignments_ref_AC");
            });

            modelBuilder.Entity<AssignmentCategory>(entity =>
            {
                entity.HasKey(e => new { e.Name, e.CatalogId, e.Semester })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0, 0 });

                entity.ToTable("AssignmentCategory");

                entity.HasIndex(e => new { e.CatalogId, e.Semester }, "catalogID");

                entity.Property(e => e.Name).HasMaxLength(100);

                entity.Property(e => e.CatalogId)
                    .HasColumnType("int(11)")
                    .HasColumnName("catalogID");

                entity.Property(e => e.Semester).HasMaxLength(10);

                entity.Property(e => e.GradingWeight)
                    .HasColumnType("int(11)")
                    .HasColumnName("gradingWeight");

                entity.HasOne(d => d.Class)
                    .WithMany(p => p.AssignmentCategories)
                    .HasForeignKey(d => new { d.CatalogId, d.Semester })
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("AssignmentCategory_ibfk_1");
            });

            modelBuilder.Entity<Class>(entity =>
            {
                entity.HasKey(e => new { e.CatalogId, e.Semester })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

                entity.HasIndex(e => e.Instructor, "classes_ref_professors");

                entity.Property(e => e.CatalogId)
                    .HasColumnType("int(11)")
                    .HasColumnName("catalogID");

                entity.Property(e => e.Semester).HasMaxLength(10);

                entity.Property(e => e.EndTime)
                    .HasColumnType("datetime")
                    .HasColumnName("endTime");

                entity.Property(e => e.Instructor)
                    .HasMaxLength(8)
                    .HasColumnName("instructor")
                    .IsFixedLength();

                entity.Property(e => e.Location).HasMaxLength(100);

                entity.Property(e => e.StartTime)
                    .HasColumnType("datetime")
                    .HasColumnName("startTime");

                entity.HasOne(d => d.Catalog)
                    .WithMany(p => p.Classes)
                    .HasForeignKey(d => d.CatalogId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Classes_ibfk_1");

                entity.HasOne(d => d.InstructorNavigation)
                    .WithMany(p => p.Classes)
                    .HasForeignKey(d => d.Instructor)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("classes_ref_professors");
            });

            modelBuilder.Entity<Course>(entity =>
            {
                entity.HasKey(e => e.CatalogId)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.ListedUnder, "Courses_ref_Departments");

                entity.Property(e => e.CatalogId)
                    .HasColumnType("int(11)")
                    .ValueGeneratedNever()
                    .HasColumnName("catalogID");

                entity.Property(e => e.ListedUnder).HasMaxLength(4);

                entity.Property(e => e.Name)
                    .HasMaxLength(100)
                    .HasColumnName("name");

                entity.Property(e => e.Number)
                    .HasColumnType("int(11)")
                    .HasColumnName("number");

                entity.HasOne(d => d.ListedUnderNavigation)
                    .WithMany(p => p.Courses)
                    .HasForeignKey(d => d.ListedUnder)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Courses_ref_Departments");
            });

            modelBuilder.Entity<Department>(entity =>
            {
                entity.HasKey(e => e.SubjectAbbr)
                    .HasName("PRIMARY");

                entity.Property(e => e.SubjectAbbr)
                    .HasMaxLength(4)
                    .HasColumnName("subjectABBR");

                entity.Property(e => e.Name).HasMaxLength(100);
            });

            modelBuilder.Entity<EnrollIn>(entity =>
            {
                entity.HasKey(e => new { e.UId, e.CatalogId, e.Semester })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0, 0 });

                entity.ToTable("EnrollIn");

                entity.HasIndex(e => new { e.CatalogId, e.Semester }, "catalogID");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.Property(e => e.CatalogId)
                    .HasColumnType("int(11)")
                    .HasColumnName("catalogID");

                entity.Property(e => e.Semester).HasMaxLength(10);

                entity.Property(e => e.Grade)
                    .HasMaxLength(2)
                    .HasColumnName("grade");

                entity.HasOne(d => d.UIdNavigation)
                    .WithMany(p => p.EnrollIns)
                    .HasForeignKey(d => d.UId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("EnrollIn_ibfk_1");

                entity.HasOne(d => d.Class)
                    .WithMany(p => p.EnrollIns)
                    .HasForeignKey(d => new { d.CatalogId, d.Semester })
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("EnrollIn_ibfk_2");
            });

            modelBuilder.Entity<Professor>(entity =>
            {
                entity.HasKey(e => e.UId)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.WorkIn, "Professors_ref_Departments");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.Property(e => e.WorkIn).HasMaxLength(4);

                entity.HasOne(d => d.UIdNavigation)
                    .WithOne(p => p.Professor)
                    .HasForeignKey<Professor>(d => d.UId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Professors_ibfk_1");

                entity.HasOne(d => d.WorkInNavigation)
                    .WithMany(p => p.Professors)
                    .HasForeignKey(d => d.WorkIn)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Professors_ref_Departments");
            });

            modelBuilder.Entity<Student>(entity =>
            {
                entity.HasKey(e => e.UId)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.MajorIn, "Students_ref_Departments");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.Property(e => e.MajorIn).HasMaxLength(4);

                entity.HasOne(d => d.MajorInNavigation)
                    .WithMany(p => p.Students)
                    .HasForeignKey(d => d.MajorIn)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Students_ref_Departments");

                entity.HasOne(d => d.UIdNavigation)
                    .WithOne(p => p.Student)
                    .HasForeignKey<Student>(d => d.UId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Students_ibfk_1");
            });

            modelBuilder.Entity<Submission>(entity =>
            {
                entity.HasKey(e => new { e.UId, e.AId })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

                entity.ToTable("Submission");

                entity.HasIndex(e => e.AId, "Submission_ref_Assignmenta");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.Property(e => e.AId)
                    .HasColumnType("int(10) unsigned")
                    .HasColumnName("aID");

                entity.Property(e => e.Contents).HasMaxLength(8192);

                entity.Property(e => e.Score).HasColumnType("int(11)");

                entity.Property(e => e.SubmissionDateTime).HasColumnType("datetime");

                entity.HasOne(d => d.AIdNavigation)
                    .WithMany(p => p.Submissions)
                    .HasForeignKey(d => d.AId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Submission_ref_Assignmenta");

                entity.HasOne(d => d.UIdNavigation)
                    .WithMany(p => p.Submissions)
                    .HasForeignKey(d => d.UId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("Submission_ref_Students");
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.HasKey(e => e.UId)
                    .HasName("PRIMARY");

                entity.Property(e => e.UId)
                    .HasMaxLength(8)
                    .HasColumnName("uID")
                    .IsFixedLength();

                entity.Property(e => e.Dob)
                    .HasColumnType("datetime")
                    .HasColumnName("DOB");

                entity.Property(e => e.FirstName)
                    .HasMaxLength(100)
                    .HasColumnName("firstName");

                entity.Property(e => e.LastName)
                    .HasMaxLength(100)
                    .HasColumnName("lastName");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
