package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.DatabaseConnection
import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.entities.*
import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AvailableTimesRequest
import diplomski.etf.bg.ac.rs.models.requests.InfoChangeRequest
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForPatientResponse
import diplomski.etf.bg.ac.rs.models.responses.AvailableTimesResponse
import diplomski.etf.bg.ac.rs.utils.Role
import kotlinx.datetime.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import java.time.DayOfWeek

class PatientDaoImpl(private val database: Database): PatientDao {
    override fun getPatientById(patientId: String): Patient? =
        database
            .from(PatientEntity)
            .select()
            .where {
                PatientEntity.id eq patientId
            }
            .map {
                Patient(
                    id = patientId,
                    email = it[PatientEntity.email]!!,
                    firstName = it[PatientEntity.first_name]!!,
                    lastName = it[PatientEntity.last_name]!!,
                    password = it[PatientEntity.password]!!,
                    phone = it[PatientEntity.phone]!!,
                    ssn = it[PatientEntity.ssn]!!
                )
            }.firstOrNull()

    override fun getAppointmentsForPatient(patientId: String): List<AppointmentForPatientResponse> =
        database
            .from(AppointmentEntity)
            .innerJoin(DoctorEntity, on = AppointmentEntity.doctor_id eq DoctorEntity.id)
            .select(
                DoctorEntity.first_name, DoctorEntity.last_name, DoctorEntity.specialization_id,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id,
                AppointmentEntity.confirmed, AppointmentEntity.cancelled_by
            )
            .where {
                AppointmentEntity.patient_id eq patientId and (AppointmentEntity.cancelled_by neq Role.PATIENT.ordinal)
            }
            .orderBy(AppointmentEntity.date.asc(), AppointmentEntity.time.asc())
            .map {
                val appointmentId = it[AppointmentEntity.id]!!
                AppointmentForPatientResponse(
                    doctorName = "${it[DoctorEntity.first_name]} ${it[DoctorEntity.last_name]}",
                    doctorSpecializationId = it[DoctorEntity.specialization_id]!!,
                    appointment = Appointment(
                        id = appointmentId,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        services = getServicesForAppointment(appointmentId),
                        confirmed = it[AppointmentEntity.confirmed]!!,
                        cancelledBy = it[AppointmentEntity.cancelled_by]!!
                    )
                )
            }

    override fun getAppointmentWithDoctorById(appointmentId: Int): AppointmentForPatientResponse? =
        database
            .from(AppointmentEntity)
            .innerJoin(DoctorEntity, on = AppointmentEntity.doctor_id eq DoctorEntity.id)
            .select(
                DoctorEntity.first_name, DoctorEntity.last_name, DoctorEntity.specialization_id,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id,
                AppointmentEntity.confirmed, AppointmentEntity.cancelled_by
            )
            .where {
                AppointmentEntity.id eq appointmentId
            }
            .orderBy(AppointmentEntity.date.asc(), AppointmentEntity.time.asc())
            .map {
                AppointmentForPatientResponse(
                    doctorName = "${it[DoctorEntity.first_name]} ${it[DoctorEntity.last_name]}",
                    doctorSpecializationId = it[DoctorEntity.specialization_id]!!,
                    appointment = Appointment(
                        id = it[AppointmentEntity.id]!!,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        services = getServicesForAppointment(appointmentId),
                        confirmed = it[AppointmentEntity.confirmed]!!,
                        cancelledBy = it[AppointmentEntity.cancelled_by]!!
                    )
                )
            }.firstOrNull()

    override fun getAllCategories(): List<Category> =
        database
            .from(CategoryEntity)
            .select()
            .map {
                Category(
                    id = it[CategoryEntity.id]!!,
                    name = it[CategoryEntity.name]!!
                )
            }

    override fun insertService(category: Category): Int =
        database.insert(CategoryEntity) {
            set(it.name, category.name)
        }

    override fun getDoctors(doctorName: String, categoryIds: List<Int>): List<DoctorForPatient> {
        val doctorNameSplit: List<String> = doctorName.split(" ")
        var query = database.from(DoctorEntity).select()
        var queryCondition: ColumnDeclaring<Boolean>? = null
        if (!categoryIds.contains(0)) {
            queryCondition = DoctorEntity.category_id inList categoryIds
        }
        if (doctorName.isNotEmpty()) {
            if (doctorNameSplit.size < 2) {
                queryCondition = if (queryCondition == null) {
                    DoctorEntity.first_name like "%${doctorNameSplit[0]}%" or (
                            DoctorEntity.last_name like "%${doctorNameSplit[0]}%"
                            )
                } else {
                    queryCondition and (
                            DoctorEntity.first_name like "%${doctorNameSplit[0]}%" or (
                                    DoctorEntity.last_name like "%${doctorNameSplit[0]}%"
                                    )
                            )
                }
            } else {
                queryCondition = if (queryCondition == null) {
                    DoctorEntity.first_name like "%${doctorNameSplit[0]}%" and (
                            DoctorEntity.last_name like "%${doctorNameSplit[1]}%"
                            ) or (
                            DoctorEntity.first_name like "%${doctorNameSplit[1]}%" and (
                                    DoctorEntity.last_name like "%${doctorNameSplit[0]}%"
                                    )
                            )
                } else {
                    queryCondition and (
                            DoctorEntity.first_name like "%${doctorNameSplit[0]}%" and (
                                    DoctorEntity.last_name like "%${doctorNameSplit[1]}%"
                                    ) or (
                                    DoctorEntity.first_name like "%${doctorNameSplit[1]}%" and (
                                            DoctorEntity.last_name like "%${doctorNameSplit[0]}%"
                                            )
                                    )
                            )
                }
            }
        }
        if (queryCondition != null) {
            query = query.where { queryCondition }
        }
        return query.map {
            DoctorForPatient(
                id = it[DoctorEntity.id]!!,
                email = it[DoctorEntity.email]!!,
                firstName = it[DoctorEntity.first_name]!!,
                lastName = it[DoctorEntity.last_name]!!,
                phone = it[DoctorEntity.phone]!!,
                serviceId = it[DoctorEntity.category_id]!!,
                specializationId = it[DoctorEntity.specialization_id]!!
            )
        }
    }

    override fun getPopularDoctors(): List<DoctorForPatient> {
        val doctors = database
            .from(DoctorEntity)
            .select()
            .map {
                DoctorForPatient(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    phone = it[DoctorEntity.phone]!!,
                    serviceId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }
        return doctors.shuffled().subList(0, if (doctors.size >= 4) 3 else doctors.size)
    }

    override fun getClinics(categoryId: Int?): List<ClinicForPatient> {
        val clinicList = database
            .from(ClinicEntity)
            .select()
            .map {
                ClinicForPatient(
                    id = it[ClinicEntity.id]!!,
                    email = it[ClinicEntity.email]!!,
                    name = it[ClinicEntity.name]!!,
                    openingTime = it[ClinicEntity.opening_time]!!.toKotlinLocalTime(),
                    workHours = it[ClinicEntity.work_hours]!!,
                    doctors = listOf()
                )
            }
        for (clinic in clinicList) {
            var doctorsQuery = database
                .from(DoctorClinicEntity)
                .innerJoin(DoctorEntity, on = DoctorClinicEntity.doctor_id eq DoctorEntity.id)
                .select()
            if (categoryId != null && categoryId != 0) {
                doctorsQuery = doctorsQuery.where {
                    DoctorEntity.category_id eq categoryId
                }
            }
            clinic.doctors = doctorsQuery.map {
                DoctorForPatient(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    phone = it[DoctorEntity.phone]!!,
                    serviceId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }
        }
        return clinicList
    }

    override fun getAvailableTimesForDoctors(availableTimesRequest: AvailableTimesRequest): AvailableTimesResponse {
        val doctorWorkTimes: List<DoctorWorkTime> =
            database
                .from(DoctorWorkTimeEntity)
                .select()
                .where {
                    DoctorWorkTimeEntity.doctor_id inList availableTimesRequest.doctorIds
                }
                .map {
                    DoctorWorkTime(
                        doctorId = it[DoctorWorkTimeEntity.doctor_id]!!,
                        clinicId = it[DoctorWorkTimeEntity.clinic_id]!!,
                        dayOfWeek = DayOfWeek.of(it[DoctorWorkTimeEntity.day_of_week]!!),
                        time = it[DoctorWorkTimeEntity.time]!!.toKotlinLocalTime()
                    )
                }
        val scheduledAppointments: List<Appointment> =
            database
                .from(AppointmentEntity)
                .select()
                .where {
                    AppointmentEntity.patient_id eq availableTimesRequest.patientId and(
                            AppointmentEntity.doctor_id inList availableTimesRequest.doctorIds
                    ) and(AppointmentEntity.confirmed eq true)
                }
                .map {
                    Appointment(
                        id = it[AppointmentEntity.id]!!,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        services = listOf(),
                        confirmed = it[AppointmentEntity.confirmed]!!,
                        cancelledBy = it[AppointmentEntity.cancelled_by]!!
                    )
                }
        return AvailableTimesResponse(
            doctorWorkTimes = doctorWorkTimes,
            scheduledAppointments = scheduledAppointments
        )
    }



    override fun getAllServicesForDoctor(doctorId: String): List<Service> =
        database
            .from(DoctorEntity)
            .innerJoin(ServiceEntity, on = DoctorEntity.category_id eq ServiceEntity.category_id)
            .select(ServiceEntity.id, ServiceEntity.name, ServiceEntity.category_id)
            .where {
                DoctorEntity.id eq doctorId
            }
            .map {
                Service(
                    id = it[ServiceEntity.id]!!,
                    name = it[ServiceEntity.name]!!,
                    categoryId = it[ServiceEntity.category_id]!!
                )
            }

    override fun scheduleAppointments(appointmentList: List<Appointment>): List<Int> {
        val idList: MutableList<Int> = mutableListOf()
        appointmentList.forEach { appointment ->
            var existingAppointmentId: Int? = null
            var isConfirmed: Boolean? = null
            database.from(AppointmentEntity)
                .select(AppointmentEntity.id, AppointmentEntity.confirmed)
                .where {
                    AppointmentEntity.date eq appointment.date.toJavaLocalDate() and(
                            AppointmentEntity.time eq appointment.time.toJavaLocalTime()
                    ) and(AppointmentEntity.patient_id eq appointment.patientId) and(
                            AppointmentEntity.doctor_id eq appointment.doctorId
                    )
                }
                .map {
                    existingAppointmentId = it[AppointmentEntity.id]!!
                    isConfirmed = it[AppointmentEntity.confirmed]!!
                }
                .firstOrNull()
            if (isConfirmed == false) {
                database.delete(AppointmentServiceEntity) {
                    AppointmentServiceEntity.appointment_id eq existingAppointmentId!!
                }
                database.delete(AppointmentEntity) { AppointmentEntity.id eq existingAppointmentId!! }
            }
            val appointmentId = database.insertAndGenerateKey(AppointmentEntity) {
                set(it.date, appointment.date.toJavaLocalDate())
                set(it.time, appointment.time.toJavaLocalTime())
                set(it.doctor_id, appointment.doctorId)
                set(it.patient_id, appointment.patientId)
                set(it.confirmed, appointment.confirmed)
                set(it.cancelled_by, appointment.cancelledBy)
            } as Int
            appointment.services.forEach { service ->
                database.insert(AppointmentServiceEntity) {
                    set(it.appointment_id, appointmentId)
                    set(it.service_id, service.id)
                }
            }
            idList.add(appointmentId)
        }
        return idList
    }

    override fun cancelAppointment(appointmentId: Int, callerRole: Int): Appointment? {
        val appointment = database
            .from(AppointmentEntity)
            .select()
            .where {
                AppointmentEntity.id eq appointmentId and(AppointmentEntity.confirmed eq true)
            }
            .map {
                Appointment(
                    id = it[AppointmentEntity.id]!!,
                    date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                    time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                    doctorId = it[AppointmentEntity.doctor_id]!!,
                    patientId = it[AppointmentEntity.patient_id]!!,
                    services = getServicesForAppointment(appointmentId),
                    confirmed = it[AppointmentEntity.confirmed]!!,
                    cancelledBy = it[AppointmentEntity.cancelled_by]!!
                )
            }.firstOrNull()
        if (appointment != null) {
            database.update(AppointmentEntity) {
                set(it.confirmed, false)
                set(it.cancelled_by, callerRole)
                where { it.id eq appointmentId }
            }
        }
        return appointment
    }

    override fun updateEmail(patientId: String, email: String): Int =
        database.update(PatientEntity) {
            set(it.email, email)
            where { it.id eq patientId }
        }

    override fun updatePassword(patientId: String, newPassword: String): Int =
        database.update(PatientEntity) {
            set(it.password, newPassword)
            where { it.id eq patientId }
        }

    override fun updateInfo(patientId: String, infoChangeRequest: InfoChangeRequest): Int =
        database.update(PatientEntity) {
            set(it.first_name, infoChangeRequest.firstName)
            set(it.last_name, infoChangeRequest.lastName)
            set(it.phone, infoChangeRequest.phone)
            set(it.ssn, infoChangeRequest.ssn)
            where { it.id eq patientId }
        }

    private fun getServicesForAppointment(appointmentId: Int): List<Service> =
        DatabaseConnection.database
            .from(AppointmentServiceEntity)
            .innerJoin(ServiceEntity, on = AppointmentServiceEntity.service_id eq ServiceEntity.id)
            .select()
            .where { AppointmentServiceEntity.appointment_id eq appointmentId }
            .map {row ->
                Service(
                    id = row[ServiceEntity.id]!!,
                    name = row[ServiceEntity.name]!!,
                    categoryId = row[ServiceEntity.category_id]!!
                )
            }
}